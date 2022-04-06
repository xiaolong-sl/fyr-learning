package com.fyr.activiti.learning.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class JsonUtil {
    private static final Logger log = LoggerFactory.getLogger(JsonUtil.class);

    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        // 对象字段全部列入
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);

        // 取消默认转换timestamps形式
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        // 忽略空bean转json的错误
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        // 忽略在json字符串中存在,但是在java对象中不存在对应属性的情况
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // 统一日期格式yyyy-MM-dd HH:mm:ss
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * object转Json字符串
     *
     * @param obj
     * @param <T>
     * @return
     */
    public static <T> String obj2String(T obj) {
        if (obj == null) {
            return null;
        }
        try {
            return obj instanceof String ? (String) obj : objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            log.error("Parse object to String error", e);
            return null;
        }
    }

    /**
     * Object转json字符串并格式化美化
     *
     * @param obj
     * @param <T>
     * @return
     */
    public static <T> String obj2StringPretty(T obj) {
        if (obj == null) {
            return null;
        }
        try {
            return obj instanceof String ? (String) obj : objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (Exception e) {
            log.error("Parse object to String error", e);
            return null;
        }
    }

    /**
     * string转object
     *
     * @param str   json字符串
     * @param clazz 被转对象class
     * @param <T>
     * @return
     */
    public static <T> T string2Obj(String str, Class<T> clazz) {
        if (StringUtils.isEmpty(str) || clazz == null) {
            return null;
        }
        try {
            return clazz.equals(String.class) ? (T) str : objectMapper.readValue(str, clazz);
        } catch (IOException e) {
            log.error("Parse String to Object error", e);
            return null;
        }
    }

    /**
     * string转object
     *
     * @param str           json字符串
     * @param typeReference 被转对象引用类型
     * @param <T>
     * @return
     */
    public static <T> T string2ObjRef(String str, TypeReference<T> typeReference) {
        if (StringUtils.isEmpty(str) || typeReference == null) {
            return null;
        }
        try {
            return (T) (typeReference.getType().equals(String.class) ? str : objectMapper.readValue(str, typeReference));
        } catch (IOException e) {
            log.error("Parse String to Object error", e);
            return null;
        }
    }

    /**
     * string转collection 用于转为集合对象
     *
     * @param str             json字符串
     * @param collectionClass 被转集合class
     * @param elementClasses  被转集合中对象类型class
     * @param <T>
     * @return
     */
    public static <T> T string2Collection(String str, Class<?> collectionClass, Class<?>... elementClasses) {
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
        try {
            return objectMapper.readValue(str, javaType);
        } catch (IOException e) {
            log.error("Parse String to Collection error", e);
            return null;
        }
    }

    /**
     * 根据JSONArray String获取到List
     *
     * @param <T>
     * @param <T>
     * @param jArrayStr
     * @return
     */
    public static <T> List<T> getListByJSONArray(Class<T> class1, String jArrayStr) throws Exception {
        List<T> list = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(jArrayStr);
        // if (jsonArray == null || jsonArray.isEmpty()) {
        if (jsonArray == null) {
            return list; // nerver return null
        }
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            T t = createObject(jsonObject, class1);
            list.add(t);
        }
        return list;
    }

    /**
     * 根据List获取到对应的JSONArray
     *
     * @param list
     * @return
     */
    public static JSONArray getJSONArrayByList(List<?> list) {
        JSONArray jsonArray = new JSONArray();
        if (list == null || list.isEmpty()) {
            return jsonArray;//nerver return null
        }
        for (Object object : list) {
            jsonArray.put(object);
        }
        return jsonArray;
    }

    /**
     * 用get方法获取数据，首字母大写，如getName()
     */
    public static String captureName(String name) {
        char[] cs = name.toCharArray();
        //ascii 码表 ，如 n=110，N=78
        cs[0] -= 32;
        return String.valueOf(cs);
    }

    /**
     * <p>判断是否为基本类型并且不为数组</p>
     */
    private static boolean isBaseType(Class<?> clazz) {
        // isPrimitive 原始类型，isAssignableFrom 判断是否为某个类的类型
        if (clazz.isPrimitive()
                || String.class.isAssignableFrom(clazz)// clazz 是否能强转为 String 类型
                || Integer.class.isAssignableFrom(clazz)
                || Double.class.isAssignableFrom(clazz)
                || Float.class.isAssignableFrom(clazz)
                || Long.class.isAssignableFrom(clazz)
                || Boolean.class.isAssignableFrom(clazz)
                || Byte.class.isAssignableFrom(clazz)
                || Short.class.isAssignableFrom(clazz)
                || Character.class.isAssignableFrom(clazz)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 创建数组
     *
     * @param jsonArray jsonArray
     * @param tClazz tClazz 表示数组类
     */
    @SuppressWarnings("unchecked")
    private static <T> T createArr(JSONArray jsonArray, Class<T> tClazz) throws Exception {
        int len = jsonArray.length();
        // System.out.println(tClazz + " " + jsonArray);
        //创建具有指定组件类型和长度的新数组
        Object arr = Array.newInstance(tClazz.getComponentType(), len);
        for (int i = 0; i < len; i++) {
            Object obj = jsonArray.get(i);
            if (isBaseType(obj.getClass()) && !obj.getClass().isArray()) {
                Array.set(arr, i, (int) obj);
            } else if (obj instanceof JSONObject) {
                JSONObject jsonObjectNext = (JSONObject) obj;
                Array.set(arr, i, createObject(jsonObjectNext, tClazz.getComponentType()));
            } else if (obj instanceof JSONArray) {
                JSONArray jsonArrayNext = (JSONArray) obj;
                Array.set(arr, i, createArr(jsonArrayNext, tClazz.getComponentType()));
            }
        }
        return (T) arr;
    }

    private static <T> T createObject(JSONObject jsonObject, Class<T> tClazz) throws Exception {
        // 创建 tClazz 对象对应类的实例
        T t = tClazz.newInstance();
        assignField(jsonObject, tClazz, t);
        return t;
    }

    private static <T> void assignField(JSONObject jsonObject, Class<?> tClazz, T t) throws Exception {
        if (tClazz == null) {
            return;
        }
        // 获得 tClazz 类声明的所有字段
        Field[] fields = tClazz.getDeclaredFields();
        for (Field field : fields) {
            // 获取 此Field对象表示的字段的名称
            String fieldName = field.getName();
            // getType()：返回一个Class 对象，它标识了此 Field 对象所表示字段的声明类型，如：String、Integer
            Class<?> filedClazz = field.getType();
            if (jsonObject.isNull(fieldName)) {
                continue;
            }
            // 获取字段fieldName对应的值value
            Object value = jsonObject.opt(fieldName);
            if (isBaseType(filedClazz) || JSONObject.class.isAssignableFrom(filedClazz)
                    || JSONArray.class.isAssignableFrom(filedClazz)) {
                setterObject(tClazz, fieldName, filedClazz, t, value);
            } else if (filedClazz.isArray()) {
                if (value instanceof JSONArray) {
                    JSONArray jsonArray = (JSONArray) value;
                    Object arr = createArr(jsonArray, filedClazz);
                    setterObject(tClazz, fieldName, filedClazz, t, arr);
                }
            } else if (List.class.isAssignableFrom(filedClazz)) {
                if (value instanceof JSONArray) {
                    JSONArray jsonArray = (JSONArray) value;
                    Type typeClass = field.getGenericType();
                    List list = createList(typeClass, jsonArray);
                    setterObject(tClazz, fieldName, filedClazz, t, list);
                }
            } else if (Set.class.isAssignableFrom(filedClazz)) {
                if (value instanceof JSONArray) {
                    JSONArray jsonArray = (JSONArray) value;
                    Type typeClass = field.getGenericType();
                    Set set = createSet(typeClass, jsonArray);
                    setterObject(tClazz, fieldName, filedClazz, t, set);
                }
            } else if (Map.class.isAssignableFrom(filedClazz)) {
                if (value instanceof JSONObject) {
                    Type typeClass = field.getGenericType();
                    JSONObject jsonObj = (JSONObject) value;
                    Map map = createMap(typeClass, jsonObj);
                    setterObject(tClazz, fieldName, filedClazz, t, map);
                }
            } else if (JSONObject.class.isAssignableFrom(filedClazz) || JSONArray.class.isAssignableFrom(filedClazz)) {
                setterObject(tClazz, fieldName, filedClazz, t, value);
            } else {
                JSONObject obj = (JSONObject) value;
                Object fieldObj = createObject(obj, filedClazz);
                setterObject(tClazz, fieldName, filedClazz, t, fieldObj);
            }
        }
        // 父类递归处理
        Class<?> superClazz = tClazz.getSuperclass();
        assignField(jsonObject, superClazz, t);
    }

    private static Class<?> getTclazz(Type type) {
        if (type instanceof Class) {
            return (Class<?>) type;
        } else {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            // getRawType()：返回原始类型Type
            return getTclazz(parameterizedType.getRawType());
        }
    }

    private static Map createMap(Type type, JSONObject jsonObject) throws Exception {
        Map<String, Object> map = new HashMap<>();
        ParameterizedType parameterizedType = (ParameterizedType) type;
        Type nextType = parameterizedType.getActualTypeArguments()[1];
        Class<?> itemKlacc = getTclazz(nextType);
        boolean flag = isBaseType(itemKlacc);
        Iterator<String> iterator = jsonObject.keys();
        while (iterator.hasNext()) {
            String key = iterator.next();
            if (flag) {
                map.put(key, jsonObject.opt(key));
            } else {
                Object obj = jsonObject.opt(key);
                if (obj instanceof JSONObject) {
                    if (JSONObject.class.isAssignableFrom(itemKlacc)) {
                        map.put(key, obj);
                    } else {
                        Object listItem = itemKlacc.newInstance();
                        JSONObject jsonObjectNext = (JSONObject) obj;
                        assignField(jsonObjectNext, itemKlacc, listItem);
                        map.put(key, listItem);
                    }
                } else if (obj instanceof JSONArray) {
                    JSONArray jsonArrayNext = (JSONArray) obj;
                    List nextList = createList(nextType, jsonArrayNext);
                    map.put(key, nextList);
                }
            }
        }
        return map;
    }

    @SuppressWarnings("unchecked")
    private static List createList(Type type, JSONArray jsonArray) throws Exception {
        Class<?> klacc = getTclazz(type);
        boolean flag = isBaseType(klacc);
        int length = jsonArray.length();
        List list = new ArrayList<>();
        ParameterizedType parameterizedType = (ParameterizedType) type;
        Type nextType = parameterizedType.getActualTypeArguments()[0];
        Class<?> itemKlacc = getTclazz(nextType);
        for (int i = 0; i < length; i++) {
            if (flag) {
                list.add(jsonArray.get(i));
            } else {
                Object obj = jsonArray.get(i);
                if (obj instanceof JSONObject) {
                    if (JSONObject.class.isAssignableFrom(itemKlacc)) {
                        list.add(obj);
                    } else {
                        Object listItem = itemKlacc.newInstance();
                        JSONObject jsonObject = (JSONObject) obj;
                        assignField(jsonObject, itemKlacc, listItem);
                        list.add(listItem);
                    }
                } else if (obj instanceof JSONArray) {
                    JSONArray jsonArrayNext = (JSONArray) obj;
                    List nextList = createList(nextType, jsonArrayNext);
                    list.add(nextList);
                }
            }
        }
        return list;
    }

    @SuppressWarnings("unchecked")
    private static Set createSet(Type type, JSONArray jsonArray) throws Exception {
        Class<?> klacc = getTclazz(type);
        boolean flag = isBaseType(klacc);
        int length = jsonArray.length();
        Set set = new HashSet();
        ParameterizedType parameterizedType = (ParameterizedType) type;
        Type nextType = parameterizedType.getActualTypeArguments()[0];
        Class<?> itemKlacc = getTclazz(nextType);
        for (int i = 0; i < length; i++) {
            if (flag) {
                set.add(jsonArray.get(i));
            } else {
                Object obj = jsonArray.get(i);
                if (obj instanceof JSONObject) {
                    if (JSONObject.class.isAssignableFrom(itemKlacc)) {
                        set.add(obj);
                    } else {
                        Object listItem = itemKlacc.newInstance();
                        JSONObject jsonObject = (JSONObject) obj;
                        assignField(jsonObject, itemKlacc, listItem);
                        set.add(listItem);
                    }
                } else if (obj instanceof JSONArray) {
                    JSONArray jsonArrayNext = (JSONArray) obj;
                    List nextList = createList(nextType, jsonArrayNext);
                    set.add(nextList);
                }
            }
        }
        return set;
    }

    private static <T> void setterObject(Class<?> tClazz, String fieldName, Class<?> paramsClazz, T t, Object param) throws Exception {
        if (param instanceof Boolean && paramsClazz.isAssignableFrom(String.class)) {
            param = String.valueOf(param);
        }
        Method method = tClazz.getDeclaredMethod("set" + captureName(fieldName), paramsClazz);
        method.invoke(t, param);
    }
}
