package useful;

import arc.func.*;
import arc.struct.Seq;
import arc.util.*;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;
import com.mongodb.client.model.*;
import com.mongodb.client.model.changestream.*;
import org.bson.Document;
import org.bson.codecs.configuration.*;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;

import java.util.concurrent.TimeUnit;

public record MongoRepository<T>(MongoCollection<T> collection) {

    public static final CodecRegistry defaultRegistry = CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build()));

    public MongoRepository(MongoDatabase database, String name, Class<T> type) {
        this(database.withCodecRegistry(defaultRegistry).getCollection(name, type));
    }

    // region get

    public T get(Bson filter) {
        return collection.find(filter).first();
    }

    public T get(Bson filter, T defaultDocument) {
        var document = collection.find(filter).first();
        return document != null ? document : defaultDocument;
    }

    public T get(Bson filter, Prov<T> defaultDocument) {
        var document = collection.find(filter).first();
        return document != null ? document : defaultDocument.get();
    }

    // endregion
    // region insert & replace

    public void insert(T document) {
        collection.insertOne(document);
    }

    public boolean replace(Bson filter, T value) {
        return replace(filter, value, true);
    }

    public boolean replace(Bson filter, T value, boolean upsert) {
        return collection.replaceOne(filter, value, new ReplaceOptions().upsert(upsert)).getModifiedCount() > 0;
    }

    // endregion
    // region delete

    public T delete(Bson filter) {
        return collection.findOneAndDelete(filter);
    }

    // endregion
    // region each

    public void each(Cons<T> cons) {
        collection.find().forEach(cons::get);
    }

    public void each(Bson filter, Cons<T> cons) {
        collection.find(filter).forEach(cons::get);
    }

    // endregion
    // region all

    public Seq<T> all() {
        return Seq.with(collection.find());
    }

    public Seq<T> all(Bson filter) {
        return Seq.with(collection.find(filter));
    }

    // endregion
    // region watch

    public void watchBeforeChange(Cons<T> result) {
        watch(iterable -> iterable.fullDocumentBeforeChange(FullDocumentBeforeChange.WHEN_AVAILABLE), stream -> {
            var before = stream.getFullDocumentBeforeChange();
            if (before == null) return;

            result.get(before);
        });
    }

    public void watchBeforeChange(OperationType type, Cons<T> result) {
        watch(iterable -> iterable.fullDocumentBeforeChange(FullDocumentBeforeChange.WHEN_AVAILABLE), type, stream -> {
            var before = stream.getFullDocumentBeforeChange();
            if (before == null) return;

            result.get(before);
        });
    }

    public void watchAfterChange(Cons<T> result) {
        watch(iterable -> iterable.fullDocument(FullDocument.UPDATE_LOOKUP), stream -> {
            var after = stream.getFullDocument();
            if (after == null) return;

            result.get(after);
        });
    }

    public void watchAfterChange(OperationType type, Cons<T> result) {
        watch(iterable -> iterable.fullDocument(FullDocument.UPDATE_LOOKUP), type, stream -> {
            var after = stream.getFullDocument();
            if (after == null) return;

            result.get(after);
        });
    }

    public void watchBeforeAfterChange(Cons2<T, T> result) {
        watch(iterable -> iterable.fullDocument(FullDocument.WHEN_AVAILABLE).fullDocumentBeforeChange(FullDocumentBeforeChange.WHEN_AVAILABLE), stream -> {
            var before = stream.getFullDocumentBeforeChange();
            if (before == null) return;

            var after = stream.getFullDocument();
            if (after == null) return;

            result.get(before, after);
        });
    }

    public void watchBeforeAfterChange(OperationType type, Cons2<T, T> result) {
        watch(iterable -> iterable.fullDocument(FullDocument.WHEN_AVAILABLE).fullDocumentBeforeChange(FullDocumentBeforeChange.WHEN_AVAILABLE), type, stream -> {
            var before = stream.getFullDocumentBeforeChange();
            if (before == null) return;

            var after = stream.getFullDocument();
            if (after == null) return;

            result.get(before, after);
        });
    }

    public void watch(Cons<ChangeStreamIterable<?>> setter, Cons<ChangeStreamDocument<T>> result) {
        watch(setter, stream -> true, result);
    }

    public void watch(Cons<ChangeStreamIterable<?>> setter, OperationType type, Cons<ChangeStreamDocument<T>> result) {
        watch(setter, stream -> stream.getOperationType() == type, result);
    }

    public void watch(Cons<ChangeStreamIterable<?>> setter, Boolf<ChangeStreamDocument<?>> filter, Cons<ChangeStreamDocument<T>> result) {
        Threads.daemon(() -> {
            try {
                var iterable = collection.watch(collection.getDocumentClass());
                setter.get(iterable);

                iterable.forEach(stream -> {
                    if (filter.get(stream))
                        result.get(stream);
                });
            } catch (Exception e) {
                Log.debug(e);
            }
        });
    }

    // endregion
    // region index

    public void ascendingIndex(String field) {
        ascendingIndex(field, new IndexOptions().name(field));
    }

    public void ascendingIndex(String field, long expireAfter) {
        ascendingIndex(field, new IndexOptions().name(field).expireAfter(expireAfter, TimeUnit.SECONDS));
    }

    public void ascendingIndex(String field, long expireAfter, TimeUnit unit) {
        ascendingIndex(field, new IndexOptions().name(field).expireAfter(expireAfter, unit));
    }

    public void ascendingIndex(String field, IndexOptions options) {
        index(Indexes.ascending(field), options);
    }

    public void descendingIndex(String field) {
        descendingIndex(field, new IndexOptions().name(field));
    }

    public void descendingIndex(String field, long expireAfter) {
        descendingIndex(field, new IndexOptions().name(field).expireAfter(expireAfter, TimeUnit.SECONDS));
    }

    public void descendingIndex(String field, long expireAfter, TimeUnit unit) {
        descendingIndex(field, new IndexOptions().name(field).expireAfter(expireAfter, unit));
    }

    public void descendingIndex(String field, IndexOptions options) {
        index(Indexes.descending(field), options);
    }

    public void index(Bson keys) {
        collection.createIndex(keys);
    }

    public void index(Bson keys, IndexOptions options) {
        collection.createIndex(keys, options);
    }

    // endregion
    // region ID

    // returns the next ID for a document
    public int generateNextID(String field) {
        return generateNextID(field, 0);
    }

    // returns the next ID for a document
    public int generateNextID(String field, int defaultValue) {
        var document = collection.find(Document.class)
                .sort(Sorts.descending(field))
                .limit(1)
                .first();

        return document == null ? defaultValue : document.getInteger(field, defaultValue) + 1;
    }

    // endregion
    // region get field

    public <T> T getField(Bson filter, String field) {
        return getField(filter, field, null);
    }

    @SuppressWarnings("unchecked")
    public <T> T getField(Bson filter, String field, T defaultValue) {
        var document = collection.find(filter, Document.class).first();
        return document != null && document.containsKey(field) ? (T) document.get(field) : defaultValue;
    }

    // endregion
}