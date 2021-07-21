package com.lhever.simpleim.router.pojo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OssFileMetadataExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public OssFileMetadataExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(String value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(String value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(String value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(String value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(String value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(String value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLike(String value) {
            addCriterion("id like", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotLike(String value) {
            addCriterion("id not like", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<String> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<String> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(String value1, String value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(String value1, String value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andBucketIsNull() {
            addCriterion("bucket is null");
            return (Criteria) this;
        }

        public Criteria andBucketIsNotNull() {
            addCriterion("bucket is not null");
            return (Criteria) this;
        }

        public Criteria andBucketEqualTo(String value) {
            addCriterion("bucket =", value, "bucket");
            return (Criteria) this;
        }

        public Criteria andBucketNotEqualTo(String value) {
            addCriterion("bucket <>", value, "bucket");
            return (Criteria) this;
        }

        public Criteria andBucketGreaterThan(String value) {
            addCriterion("bucket >", value, "bucket");
            return (Criteria) this;
        }

        public Criteria andBucketGreaterThanOrEqualTo(String value) {
            addCriterion("bucket >=", value, "bucket");
            return (Criteria) this;
        }

        public Criteria andBucketLessThan(String value) {
            addCriterion("bucket <", value, "bucket");
            return (Criteria) this;
        }

        public Criteria andBucketLessThanOrEqualTo(String value) {
            addCriterion("bucket <=", value, "bucket");
            return (Criteria) this;
        }

        public Criteria andBucketLike(String value) {
            addCriterion("bucket like", value, "bucket");
            return (Criteria) this;
        }

        public Criteria andBucketNotLike(String value) {
            addCriterion("bucket not like", value, "bucket");
            return (Criteria) this;
        }

        public Criteria andBucketIn(List<String> values) {
            addCriterion("bucket in", values, "bucket");
            return (Criteria) this;
        }

        public Criteria andBucketNotIn(List<String> values) {
            addCriterion("bucket not in", values, "bucket");
            return (Criteria) this;
        }

        public Criteria andBucketBetween(String value1, String value2) {
            addCriterion("bucket between", value1, value2, "bucket");
            return (Criteria) this;
        }

        public Criteria andBucketNotBetween(String value1, String value2) {
            addCriterion("bucket not between", value1, value2, "bucket");
            return (Criteria) this;
        }

        public Criteria andEtagIsNull() {
            addCriterion("etag is null");
            return (Criteria) this;
        }

        public Criteria andEtagIsNotNull() {
            addCriterion("etag is not null");
            return (Criteria) this;
        }

        public Criteria andEtagEqualTo(String value) {
            addCriterion("etag =", value, "etag");
            return (Criteria) this;
        }

        public Criteria andEtagNotEqualTo(String value) {
            addCriterion("etag <>", value, "etag");
            return (Criteria) this;
        }

        public Criteria andEtagGreaterThan(String value) {
            addCriterion("etag >", value, "etag");
            return (Criteria) this;
        }

        public Criteria andEtagGreaterThanOrEqualTo(String value) {
            addCriterion("etag >=", value, "etag");
            return (Criteria) this;
        }

        public Criteria andEtagLessThan(String value) {
            addCriterion("etag <", value, "etag");
            return (Criteria) this;
        }

        public Criteria andEtagLessThanOrEqualTo(String value) {
            addCriterion("etag <=", value, "etag");
            return (Criteria) this;
        }

        public Criteria andEtagLike(String value) {
            addCriterion("etag like", value, "etag");
            return (Criteria) this;
        }

        public Criteria andEtagNotLike(String value) {
            addCriterion("etag not like", value, "etag");
            return (Criteria) this;
        }

        public Criteria andEtagIn(List<String> values) {
            addCriterion("etag in", values, "etag");
            return (Criteria) this;
        }

        public Criteria andEtagNotIn(List<String> values) {
            addCriterion("etag not in", values, "etag");
            return (Criteria) this;
        }

        public Criteria andEtagBetween(String value1, String value2) {
            addCriterion("etag between", value1, value2, "etag");
            return (Criteria) this;
        }

        public Criteria andEtagNotBetween(String value1, String value2) {
            addCriterion("etag not between", value1, value2, "etag");
            return (Criteria) this;
        }

        public Criteria andStoreFileNameIsNull() {
            addCriterion("store_file_name is null");
            return (Criteria) this;
        }

        public Criteria andStoreFileNameIsNotNull() {
            addCriterion("store_file_name is not null");
            return (Criteria) this;
        }

        public Criteria andStoreFileNameEqualTo(String value) {
            addCriterion("store_file_name =", value, "storeFileName");
            return (Criteria) this;
        }

        public Criteria andStoreFileNameNotEqualTo(String value) {
            addCriterion("store_file_name <>", value, "storeFileName");
            return (Criteria) this;
        }

        public Criteria andStoreFileNameGreaterThan(String value) {
            addCriterion("store_file_name >", value, "storeFileName");
            return (Criteria) this;
        }

        public Criteria andStoreFileNameGreaterThanOrEqualTo(String value) {
            addCriterion("store_file_name >=", value, "storeFileName");
            return (Criteria) this;
        }

        public Criteria andStoreFileNameLessThan(String value) {
            addCriterion("store_file_name <", value, "storeFileName");
            return (Criteria) this;
        }

        public Criteria andStoreFileNameLessThanOrEqualTo(String value) {
            addCriterion("store_file_name <=", value, "storeFileName");
            return (Criteria) this;
        }

        public Criteria andStoreFileNameLike(String value) {
            addCriterion("store_file_name like", value, "storeFileName");
            return (Criteria) this;
        }

        public Criteria andStoreFileNameNotLike(String value) {
            addCriterion("store_file_name not like", value, "storeFileName");
            return (Criteria) this;
        }

        public Criteria andStoreFileNameIn(List<String> values) {
            addCriterion("store_file_name in", values, "storeFileName");
            return (Criteria) this;
        }

        public Criteria andStoreFileNameNotIn(List<String> values) {
            addCriterion("store_file_name not in", values, "storeFileName");
            return (Criteria) this;
        }

        public Criteria andStoreFileNameBetween(String value1, String value2) {
            addCriterion("store_file_name between", value1, value2, "storeFileName");
            return (Criteria) this;
        }

        public Criteria andStoreFileNameNotBetween(String value1, String value2) {
            addCriterion("store_file_name not between", value1, value2, "storeFileName");
            return (Criteria) this;
        }

        public Criteria andOriginalFileNameIsNull() {
            addCriterion("original_file_name is null");
            return (Criteria) this;
        }

        public Criteria andOriginalFileNameIsNotNull() {
            addCriterion("original_file_name is not null");
            return (Criteria) this;
        }

        public Criteria andOriginalFileNameEqualTo(String value) {
            addCriterion("original_file_name =", value, "originalFileName");
            return (Criteria) this;
        }

        public Criteria andOriginalFileNameNotEqualTo(String value) {
            addCriterion("original_file_name <>", value, "originalFileName");
            return (Criteria) this;
        }

        public Criteria andOriginalFileNameGreaterThan(String value) {
            addCriterion("original_file_name >", value, "originalFileName");
            return (Criteria) this;
        }

        public Criteria andOriginalFileNameGreaterThanOrEqualTo(String value) {
            addCriterion("original_file_name >=", value, "originalFileName");
            return (Criteria) this;
        }

        public Criteria andOriginalFileNameLessThan(String value) {
            addCriterion("original_file_name <", value, "originalFileName");
            return (Criteria) this;
        }

        public Criteria andOriginalFileNameLessThanOrEqualTo(String value) {
            addCriterion("original_file_name <=", value, "originalFileName");
            return (Criteria) this;
        }

        public Criteria andOriginalFileNameLike(String value) {
            addCriterion("original_file_name like", value, "originalFileName");
            return (Criteria) this;
        }

        public Criteria andOriginalFileNameNotLike(String value) {
            addCriterion("original_file_name not like", value, "originalFileName");
            return (Criteria) this;
        }

        public Criteria andOriginalFileNameIn(List<String> values) {
            addCriterion("original_file_name in", values, "originalFileName");
            return (Criteria) this;
        }

        public Criteria andOriginalFileNameNotIn(List<String> values) {
            addCriterion("original_file_name not in", values, "originalFileName");
            return (Criteria) this;
        }

        public Criteria andOriginalFileNameBetween(String value1, String value2) {
            addCriterion("original_file_name between", value1, value2, "originalFileName");
            return (Criteria) this;
        }

        public Criteria andOriginalFileNameNotBetween(String value1, String value2) {
            addCriterion("original_file_name not between", value1, value2, "originalFileName");
            return (Criteria) this;
        }

        public Criteria andFileExtensionIsNull() {
            addCriterion("file_extension is null");
            return (Criteria) this;
        }

        public Criteria andFileExtensionIsNotNull() {
            addCriterion("file_extension is not null");
            return (Criteria) this;
        }

        public Criteria andFileExtensionEqualTo(String value) {
            addCriterion("file_extension =", value, "fileExtension");
            return (Criteria) this;
        }

        public Criteria andFileExtensionNotEqualTo(String value) {
            addCriterion("file_extension <>", value, "fileExtension");
            return (Criteria) this;
        }

        public Criteria andFileExtensionGreaterThan(String value) {
            addCriterion("file_extension >", value, "fileExtension");
            return (Criteria) this;
        }

        public Criteria andFileExtensionGreaterThanOrEqualTo(String value) {
            addCriterion("file_extension >=", value, "fileExtension");
            return (Criteria) this;
        }

        public Criteria andFileExtensionLessThan(String value) {
            addCriterion("file_extension <", value, "fileExtension");
            return (Criteria) this;
        }

        public Criteria andFileExtensionLessThanOrEqualTo(String value) {
            addCriterion("file_extension <=", value, "fileExtension");
            return (Criteria) this;
        }

        public Criteria andFileExtensionLike(String value) {
            addCriterion("file_extension like", value, "fileExtension");
            return (Criteria) this;
        }

        public Criteria andFileExtensionNotLike(String value) {
            addCriterion("file_extension not like", value, "fileExtension");
            return (Criteria) this;
        }

        public Criteria andFileExtensionIn(List<String> values) {
            addCriterion("file_extension in", values, "fileExtension");
            return (Criteria) this;
        }

        public Criteria andFileExtensionNotIn(List<String> values) {
            addCriterion("file_extension not in", values, "fileExtension");
            return (Criteria) this;
        }

        public Criteria andFileExtensionBetween(String value1, String value2) {
            addCriterion("file_extension between", value1, value2, "fileExtension");
            return (Criteria) this;
        }

        public Criteria andFileExtensionNotBetween(String value1, String value2) {
            addCriterion("file_extension not between", value1, value2, "fileExtension");
            return (Criteria) this;
        }

        public Criteria andMediaTypeIsNull() {
            addCriterion("media_type is null");
            return (Criteria) this;
        }

        public Criteria andMediaTypeIsNotNull() {
            addCriterion("media_type is not null");
            return (Criteria) this;
        }

        public Criteria andMediaTypeEqualTo(String value) {
            addCriterion("media_type =", value, "mediaType");
            return (Criteria) this;
        }

        public Criteria andMediaTypeNotEqualTo(String value) {
            addCriterion("media_type <>", value, "mediaType");
            return (Criteria) this;
        }

        public Criteria andMediaTypeGreaterThan(String value) {
            addCriterion("media_type >", value, "mediaType");
            return (Criteria) this;
        }

        public Criteria andMediaTypeGreaterThanOrEqualTo(String value) {
            addCriterion("media_type >=", value, "mediaType");
            return (Criteria) this;
        }

        public Criteria andMediaTypeLessThan(String value) {
            addCriterion("media_type <", value, "mediaType");
            return (Criteria) this;
        }

        public Criteria andMediaTypeLessThanOrEqualTo(String value) {
            addCriterion("media_type <=", value, "mediaType");
            return (Criteria) this;
        }

        public Criteria andMediaTypeLike(String value) {
            addCriterion("media_type like", value, "mediaType");
            return (Criteria) this;
        }

        public Criteria andMediaTypeNotLike(String value) {
            addCriterion("media_type not like", value, "mediaType");
            return (Criteria) this;
        }

        public Criteria andMediaTypeIn(List<String> values) {
            addCriterion("media_type in", values, "mediaType");
            return (Criteria) this;
        }

        public Criteria andMediaTypeNotIn(List<String> values) {
            addCriterion("media_type not in", values, "mediaType");
            return (Criteria) this;
        }

        public Criteria andMediaTypeBetween(String value1, String value2) {
            addCriterion("media_type between", value1, value2, "mediaType");
            return (Criteria) this;
        }

        public Criteria andMediaTypeNotBetween(String value1, String value2) {
            addCriterion("media_type not between", value1, value2, "mediaType");
            return (Criteria) this;
        }

        public Criteria andFileSizeIsNull() {
            addCriterion("file_size is null");
            return (Criteria) this;
        }

        public Criteria andFileSizeIsNotNull() {
            addCriterion("file_size is not null");
            return (Criteria) this;
        }

        public Criteria andFileSizeEqualTo(Long value) {
            addCriterion("file_size =", value, "fileSize");
            return (Criteria) this;
        }

        public Criteria andFileSizeNotEqualTo(Long value) {
            addCriterion("file_size <>", value, "fileSize");
            return (Criteria) this;
        }

        public Criteria andFileSizeGreaterThan(Long value) {
            addCriterion("file_size >", value, "fileSize");
            return (Criteria) this;
        }

        public Criteria andFileSizeGreaterThanOrEqualTo(Long value) {
            addCriterion("file_size >=", value, "fileSize");
            return (Criteria) this;
        }

        public Criteria andFileSizeLessThan(Long value) {
            addCriterion("file_size <", value, "fileSize");
            return (Criteria) this;
        }

        public Criteria andFileSizeLessThanOrEqualTo(Long value) {
            addCriterion("file_size <=", value, "fileSize");
            return (Criteria) this;
        }

        public Criteria andFileSizeIn(List<Long> values) {
            addCriterion("file_size in", values, "fileSize");
            return (Criteria) this;
        }

        public Criteria andFileSizeNotIn(List<Long> values) {
            addCriterion("file_size not in", values, "fileSize");
            return (Criteria) this;
        }

        public Criteria andFileSizeBetween(Long value1, Long value2) {
            addCriterion("file_size between", value1, value2, "fileSize");
            return (Criteria) this;
        }

        public Criteria andFileSizeNotBetween(Long value1, Long value2) {
            addCriterion("file_size not between", value1, value2, "fileSize");
            return (Criteria) this;
        }

        public Criteria andFilePathIsNull() {
            addCriterion("file_path is null");
            return (Criteria) this;
        }

        public Criteria andFilePathIsNotNull() {
            addCriterion("file_path is not null");
            return (Criteria) this;
        }

        public Criteria andFilePathEqualTo(String value) {
            addCriterion("file_path =", value, "filePath");
            return (Criteria) this;
        }

        public Criteria andFilePathNotEqualTo(String value) {
            addCriterion("file_path <>", value, "filePath");
            return (Criteria) this;
        }

        public Criteria andFilePathGreaterThan(String value) {
            addCriterion("file_path >", value, "filePath");
            return (Criteria) this;
        }

        public Criteria andFilePathGreaterThanOrEqualTo(String value) {
            addCriterion("file_path >=", value, "filePath");
            return (Criteria) this;
        }

        public Criteria andFilePathLessThan(String value) {
            addCriterion("file_path <", value, "filePath");
            return (Criteria) this;
        }

        public Criteria andFilePathLessThanOrEqualTo(String value) {
            addCriterion("file_path <=", value, "filePath");
            return (Criteria) this;
        }

        public Criteria andFilePathLike(String value) {
            addCriterion("file_path like", value, "filePath");
            return (Criteria) this;
        }

        public Criteria andFilePathNotLike(String value) {
            addCriterion("file_path not like", value, "filePath");
            return (Criteria) this;
        }

        public Criteria andFilePathIn(List<String> values) {
            addCriterion("file_path in", values, "filePath");
            return (Criteria) this;
        }

        public Criteria andFilePathNotIn(List<String> values) {
            addCriterion("file_path not in", values, "filePath");
            return (Criteria) this;
        }

        public Criteria andFilePathBetween(String value1, String value2) {
            addCriterion("file_path between", value1, value2, "filePath");
            return (Criteria) this;
        }

        public Criteria andFilePathNotBetween(String value1, String value2) {
            addCriterion("file_path not between", value1, value2, "filePath");
            return (Criteria) this;
        }

        public Criteria andOriginalUrlIsNull() {
            addCriterion("original_url is null");
            return (Criteria) this;
        }

        public Criteria andOriginalUrlIsNotNull() {
            addCriterion("original_url is not null");
            return (Criteria) this;
        }

        public Criteria andOriginalUrlEqualTo(String value) {
            addCriterion("original_url =", value, "originalUrl");
            return (Criteria) this;
        }

        public Criteria andOriginalUrlNotEqualTo(String value) {
            addCriterion("original_url <>", value, "originalUrl");
            return (Criteria) this;
        }

        public Criteria andOriginalUrlGreaterThan(String value) {
            addCriterion("original_url >", value, "originalUrl");
            return (Criteria) this;
        }

        public Criteria andOriginalUrlGreaterThanOrEqualTo(String value) {
            addCriterion("original_url >=", value, "originalUrl");
            return (Criteria) this;
        }

        public Criteria andOriginalUrlLessThan(String value) {
            addCriterion("original_url <", value, "originalUrl");
            return (Criteria) this;
        }

        public Criteria andOriginalUrlLessThanOrEqualTo(String value) {
            addCriterion("original_url <=", value, "originalUrl");
            return (Criteria) this;
        }

        public Criteria andOriginalUrlLike(String value) {
            addCriterion("original_url like", value, "originalUrl");
            return (Criteria) this;
        }

        public Criteria andOriginalUrlNotLike(String value) {
            addCriterion("original_url not like", value, "originalUrl");
            return (Criteria) this;
        }

        public Criteria andOriginalUrlIn(List<String> values) {
            addCriterion("original_url in", values, "originalUrl");
            return (Criteria) this;
        }

        public Criteria andOriginalUrlNotIn(List<String> values) {
            addCriterion("original_url not in", values, "originalUrl");
            return (Criteria) this;
        }

        public Criteria andOriginalUrlBetween(String value1, String value2) {
            addCriterion("original_url between", value1, value2, "originalUrl");
            return (Criteria) this;
        }

        public Criteria andOriginalUrlNotBetween(String value1, String value2) {
            addCriterion("original_url not between", value1, value2, "originalUrl");
            return (Criteria) this;
        }

        public Criteria andReplacedUrlIsNull() {
            addCriterion("replaced_url is null");
            return (Criteria) this;
        }

        public Criteria andReplacedUrlIsNotNull() {
            addCriterion("replaced_url is not null");
            return (Criteria) this;
        }

        public Criteria andReplacedUrlEqualTo(String value) {
            addCriterion("replaced_url =", value, "replacedUrl");
            return (Criteria) this;
        }

        public Criteria andReplacedUrlNotEqualTo(String value) {
            addCriterion("replaced_url <>", value, "replacedUrl");
            return (Criteria) this;
        }

        public Criteria andReplacedUrlGreaterThan(String value) {
            addCriterion("replaced_url >", value, "replacedUrl");
            return (Criteria) this;
        }

        public Criteria andReplacedUrlGreaterThanOrEqualTo(String value) {
            addCriterion("replaced_url >=", value, "replacedUrl");
            return (Criteria) this;
        }

        public Criteria andReplacedUrlLessThan(String value) {
            addCriterion("replaced_url <", value, "replacedUrl");
            return (Criteria) this;
        }

        public Criteria andReplacedUrlLessThanOrEqualTo(String value) {
            addCriterion("replaced_url <=", value, "replacedUrl");
            return (Criteria) this;
        }

        public Criteria andReplacedUrlLike(String value) {
            addCriterion("replaced_url like", value, "replacedUrl");
            return (Criteria) this;
        }

        public Criteria andReplacedUrlNotLike(String value) {
            addCriterion("replaced_url not like", value, "replacedUrl");
            return (Criteria) this;
        }

        public Criteria andReplacedUrlIn(List<String> values) {
            addCriterion("replaced_url in", values, "replacedUrl");
            return (Criteria) this;
        }

        public Criteria andReplacedUrlNotIn(List<String> values) {
            addCriterion("replaced_url not in", values, "replacedUrl");
            return (Criteria) this;
        }

        public Criteria andReplacedUrlBetween(String value1, String value2) {
            addCriterion("replaced_url between", value1, value2, "replacedUrl");
            return (Criteria) this;
        }

        public Criteria andReplacedUrlNotBetween(String value1, String value2) {
            addCriterion("replaced_url not between", value1, value2, "replacedUrl");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNull() {
            addCriterion("create_time is null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNotNull() {
            addCriterion("create_time is not null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeEqualTo(Date value) {
            addCriterion("create_time =", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotEqualTo(Date value) {
            addCriterion("create_time <>", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThan(Date value) {
            addCriterion("create_time >", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("create_time >=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThan(Date value) {
            addCriterion("create_time <", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThanOrEqualTo(Date value) {
            addCriterion("create_time <=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIn(List<Date> values) {
            addCriterion("create_time in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotIn(List<Date> values) {
            addCriterion("create_time not in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeBetween(Date value1, Date value2) {
            addCriterion("create_time between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotBetween(Date value1, Date value2) {
            addCriterion("create_time not between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNull() {
            addCriterion("update_time is null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNotNull() {
            addCriterion("update_time is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeEqualTo(Date value) {
            addCriterion("update_time =", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotEqualTo(Date value) {
            addCriterion("update_time <>", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThan(Date value) {
            addCriterion("update_time >", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("update_time >=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThan(Date value) {
            addCriterion("update_time <", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThanOrEqualTo(Date value) {
            addCriterion("update_time <=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIn(List<Date> values) {
            addCriterion("update_time in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotIn(List<Date> values) {
            addCriterion("update_time not in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeBetween(Date value1, Date value2) {
            addCriterion("update_time between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotBetween(Date value1, Date value2) {
            addCriterion("update_time not between", value1, value2, "updateTime");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}