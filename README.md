# EASY-UTIL 
这是一个致力于简单的工具类库，方便开发人员快速使用。
## 类型转化 Convert

1、转化为字符串

```java
String str = Convert.toStr(1);
//str 为1

String str = Convert.toStr(null);
//str 为 "" 空字符串

String str = Convert.toStr(object);
//str 为object.toString()
```

2、转化为日期字符串

```java
//date 为常见的日期对象如：Date、LocalDateTime、TimeStamp
String dateTimeStr = Convert.toDateTimeStr(new Date());
//2024-12-18 23:57:36

```

## 集合工具类 CollUtils

1、判断非空

```java
boolean flag = CollUtil.isNotEmpty();
```

2、集合抽样

```java

    /**
     * 集合抽样
     *
     * @param collection 集合
     * @param <T>        集合元素类型
     * @param sampleSize 样本数量
     * @return 抽样后的集合
     */

public static <T> List<T> sampling(Collection<T> collection, int sampleSize);

//返回结果从集合中抽取不重复的样本
```

3、将B集合中的每个元素加入到A集合

```java
    /**
     * 加入全部
     *
     * @param <T>        集合元素类型
     * @param collection 被加入的集合 Collection
     * @param iterable   要加入的内容 Iterable
     */
    public static <T> void addAll(Collection<T> collection, Collection<T> iterable) {
        if (iterable != null) {
            collection.addAll(iterable);
        }
    }
// collection中包含了collection的元素和iterable的元素
```

## Excel工具类 ExcelUtils

