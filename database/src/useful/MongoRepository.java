package useful;

import arc.func.Boolf;
import arc.func.Cons;
import arc.func.Cons2;
import arc.struct.Seq;
import arc.util.Log;
import arc.util.Threads;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;
import com.mongodb.client.model.*;
import com.mongodb.client.model.changestream.*;
import org.bson.Document;
import org.bson.codecs.configuration.*;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;

import java.util.concurrent.TimeUnit;
import java.util.function.*;

public record MongoRepository<T>(MongoCollection<T> collection) {

    public static final CodecRegistry defaultRegistry = CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build()));

    public MongoRepository(MongoDatabase database, String name, Class<T> type) {
        this(database.withCodecRegistry(defaultRegistry).getCollection(name, type));
    }

    // region get

    public T get(String field, Object value) {
        return get(Filters.eq(field, value));
    }

    public T get(String field, Object value, T defaultDocument) {
        return get(Filters.eq(field, value), defaultDocument);
    }

    public T getAnd(String field1, Object value1, String field2, Object value2) {
        return get(Filters.and(Filters.eq(field1, value1), Filters.eq(field2, value2)));
    }

    public T getAnd(String field1, Object value1, String field2, Object value2, T defaultDocument) {
        return get(Filters.and(Filters.eq(field1, value1), Filters.eq(field2, value2)), defaultDocument);
    }

    public T getAnd(String field1, Object value1, String field2, Object value2, String field3, Object value3) {
        return get(Filters.and(Filters.eq(field1, value1), Filters.eq(field2, value2), Filters.eq(field3, value3)));
    }

    public T getAnd(String field1, Object value1, String field2, Object value2, String field3, Object value3, T defaultDocument) {
        return get(Filters.and(Filters.eq(field1, value1), Filters.eq(field2, value2), Filters.eq(field3, value3)), defaultDocument);
    }

    public T getOr(String field1, Object value1, String field2, Object value2) {
        return get(Filters.or(Filters.eq(field1, value1), Filters.eq(field2, value2)));
    }

    public T getOr(String field1, Object value1, String field2, Object value2, T defaultDocument) {
        return get(Filters.or(Filters.eq(field1, value1), Filters.eq(field2, value2)), defaultDocument);
    }

    public T getOr(String field1, Object value1, String field2, Object value2, String field3, Object value3) {
        return get(Filters.or(Filters.eq(field1, value1), Filters.eq(field2, value2), Filters.eq(field3, value3)));
    }

    public T getOr(String field1, Object value1, String field2, Object value2, String field3, Object value3, T defaultDocument) {
        return get(Filters.or(Filters.eq(field1, value1), Filters.eq(field2, value2), Filters.eq(field3, value3)), defaultDocument);
    }

    public T get(Bson filter) {
        return get(filter, null);
    }

    public T get(Bson filter, T defaultDocument) {
        var document = collection.find(filter).first();
        return document != null ? document : defaultDocument;
    }

    // endregion
    // region insert & replace

    public void insert(T document) {
        collection.insertOne(document);
    }

    public boolean replace(String field, Object value, T document) {
        return replace(Filters.eq(field, value), document);
    }

    public boolean replace(String field, Object value, T document, boolean upsert) {
        return replace(Filters.eq(field, value), document, upsert);
    }

    public boolean replaceAnd(String field1, Object value1, String field2, Object value2, T document) {
        return replace(Filters.and(Filters.eq(field1, value1), Filters.eq(field2, value2)), document);
    }

    public boolean replaceAnd(String field1, Object value1, String field2, Object value2, T document, boolean upsert) {
        return replace(Filters.and(Filters.eq(field1, value1), Filters.eq(field2, value2)), document, upsert);
    }

    public boolean replaceAnd(String field1, Object value1, String field2, Object value2, String field3, Object value3, T document) {
        return replace(Filters.and(Filters.eq(field1, value1), Filters.eq(field2, value2), Filters.eq(field3, value3)), document);
    }

    public boolean replaceAnd(String field1, Object value1, String field2, Object value2, String field3, Object value3, T document, boolean upsert) {
        return replace(Filters.and(Filters.eq(field1, value1), Filters.eq(field2, value2), Filters.eq(field3, value3)), document, upsert);
    }

    public boolean replaceOr(String field1, Object value1, String field2, Object value2, T document) {
        return replace(Filters.or(Filters.eq(field1, value1), Filters.eq(field2, value2)), document);
    }

    public boolean replaceOr(String field1, Object value1, String field2, Object value2, T document, boolean upsert) {
        return replace(Filters.or(Filters.eq(field1, value1), Filters.eq(field2, value2)), document, upsert);
    }

    public boolean replaceOr(String field1, Object value1, String field2, Object value2, String field3, Object value3, T document) {
        return replace(Filters.or(Filters.eq(field1, value1), Filters.eq(field2, value2), Filters.eq(field3, value3)), document);
    }

    public boolean replaceOr(String field1, Object value1, String field2, Object value2, String field3, Object value3, T document, boolean upsert) {
        return replace(Filters.or(Filters.eq(field1, value1), Filters.eq(field2, value2), Filters.eq(field3, value3)), document, upsert);
    }

    public boolean replace(Bson filter, T value) {
        return replace(filter, value, true);
    }

    public boolean replace(Bson filter, T value, boolean upsert) {
        return collection.replaceOne(filter, value, new ReplaceOptions().upsert(upsert)).getModifiedCount() > 0;
    }

    // endregion
    // region delete

    public T delete(String field, Object value) {
        return delete(Filters.eq(field, value));
    }

    public T deleteAnd(String field1, Object value1, String field2, Object value2) {
        return delete(Filters.and(Filters.eq(field1, value1), Filters.eq(field2, value2)));
    }

    public T deleteAnd(String field1, Object value1, String field2, Object value2, String field3, Object value3) {
        return delete(Filters.and(Filters.eq(field1, value1), Filters.eq(field2, value2), Filters.eq(field3, value3)));
    }

    public T deleteOr(String field1, Object value1, String field2, Object value2) {
        return delete(Filters.or(Filters.eq(field1, value1), Filters.eq(field2, value2)));
    }

    public T deleteOr(String field1, Object value1, String field2, Object value2, String field3, Object value3) {
        return delete(Filters.or(Filters.eq(field1, value1), Filters.eq(field2, value2), Filters.eq(field3, value3)));
    }

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
    public int generateNextID() {
        return generateNextID("id");
    }

    // returns the next ID for a document
    public int generateNextID(String field) {
        var document = collection.find(Document.class)
                .sort(Sorts.descending(field))
                .limit(1)
                .first();

        return document == null ? 0 : document.getInteger(field) + 1;
    }

    // endregion
}