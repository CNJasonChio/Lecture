package com.jasonchio.lecture.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "COMMENT_DB".
*/
public class CommentDBDao extends AbstractDao<CommentDB, Long> {

    public static final String TABLENAME = "COMMENT_DB";

    /**
     * Properties of entity CommentDB.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property CommentId = new Property(0, long.class, "commentId", true, "_id");
        public final static Property CommentuserName = new Property(1, String.class, "commentuserName", false, "COMMENTUSER_NAME");
        public final static Property UserHead = new Property(2, String.class, "userHead", false, "USER_HEAD");
        public final static Property CommentlecureId = new Property(3, long.class, "commentlecureId", false, "COMMENTLECURE_ID");
        public final static Property CommentContent = new Property(4, String.class, "commentContent", false, "COMMENT_CONTENT");
        public final static Property CommentTime = new Property(5, String.class, "commentTime", false, "COMMENT_TIME");
        public final static Property CommentLikers = new Property(6, int.class, "commentLikers", false, "COMMENT_LIKERS");
        public final static Property IsLike = new Property(7, int.class, "isLike", false, "IS_LIKE");
    }


    public CommentDBDao(DaoConfig config) {
        super(config);
    }
    
    public CommentDBDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"COMMENT_DB\" (" + //
                "\"_id\" INTEGER PRIMARY KEY NOT NULL ," + // 0: commentId
                "\"COMMENTUSER_NAME\" TEXT," + // 1: commentuserName
                "\"USER_HEAD\" TEXT," + // 2: userHead
                "\"COMMENTLECURE_ID\" INTEGER NOT NULL ," + // 3: commentlecureId
                "\"COMMENT_CONTENT\" TEXT," + // 4: commentContent
                "\"COMMENT_TIME\" TEXT," + // 5: commentTime
                "\"COMMENT_LIKERS\" INTEGER NOT NULL ," + // 6: commentLikers
                "\"IS_LIKE\" INTEGER NOT NULL );"); // 7: isLike
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"COMMENT_DB\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, CommentDB entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getCommentId());
 
        String commentuserName = entity.getCommentuserName();
        if (commentuserName != null) {
            stmt.bindString(2, commentuserName);
        }
 
        String userHead = entity.getUserHead();
        if (userHead != null) {
            stmt.bindString(3, userHead);
        }
        stmt.bindLong(4, entity.getCommentlecureId());
 
        String commentContent = entity.getCommentContent();
        if (commentContent != null) {
            stmt.bindString(5, commentContent);
        }
 
        String commentTime = entity.getCommentTime();
        if (commentTime != null) {
            stmt.bindString(6, commentTime);
        }
        stmt.bindLong(7, entity.getCommentLikers());
        stmt.bindLong(8, entity.getIsLike());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, CommentDB entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getCommentId());
 
        String commentuserName = entity.getCommentuserName();
        if (commentuserName != null) {
            stmt.bindString(2, commentuserName);
        }
 
        String userHead = entity.getUserHead();
        if (userHead != null) {
            stmt.bindString(3, userHead);
        }
        stmt.bindLong(4, entity.getCommentlecureId());
 
        String commentContent = entity.getCommentContent();
        if (commentContent != null) {
            stmt.bindString(5, commentContent);
        }
 
        String commentTime = entity.getCommentTime();
        if (commentTime != null) {
            stmt.bindString(6, commentTime);
        }
        stmt.bindLong(7, entity.getCommentLikers());
        stmt.bindLong(8, entity.getIsLike());
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.getLong(offset + 0);
    }    

    @Override
    public CommentDB readEntity(Cursor cursor, int offset) {
        CommentDB entity = new CommentDB( //
            cursor.getLong(offset + 0), // commentId
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // commentuserName
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // userHead
            cursor.getLong(offset + 3), // commentlecureId
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // commentContent
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // commentTime
            cursor.getInt(offset + 6), // commentLikers
            cursor.getInt(offset + 7) // isLike
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, CommentDB entity, int offset) {
        entity.setCommentId(cursor.getLong(offset + 0));
        entity.setCommentuserName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setUserHead(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setCommentlecureId(cursor.getLong(offset + 3));
        entity.setCommentContent(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setCommentTime(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setCommentLikers(cursor.getInt(offset + 6));
        entity.setIsLike(cursor.getInt(offset + 7));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(CommentDB entity, long rowId) {
        entity.setCommentId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(CommentDB entity) {
        if(entity != null) {
            return entity.getCommentId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(CommentDB entity) {
        throw new UnsupportedOperationException("Unsupported for entities with a non-null key");
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
