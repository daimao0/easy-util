# EASY-UTIL 
这是一个致力于简单的工具类库，方便开发人员快速使用。
## 类型转化 Convert

1、转化为字符串

```java
import com.easy.convert.Convert;

String str = Convert.toStr(1);
//str 为1

String str = Convert.toStr(null);
//str 为 "" 空字符串

String str = Convert.toStr(object);
//str 为object.toString()

//可以将任意格式的日期字符串转化为日期对象
Date date = Convert.toDate("2024/12-18 23时57分36秒");
// Date对象Wed Dec 18 23:57:36 CST 2024

```

2、转化为日期字符串

```java
//date 为常见的日期对象如：Date、LocalDateTime、TimeStamp
String dateTimeStr = Convert.toDateTimeStr(new Date());
//2024-12-18 23:57:36

```
## 日期工具类 DateUtils

```java
//可以将任意格式的日期字符串转化为日期对象
Date date = DateUtils.toDate("2024-12-18 23:57:36");
Date date = DateUtils.toDate("2024/12/18 23:57:36");
Date date = DateUtils.toDate("2024/12-18 23时57分36秒");
// Date对象Wed Dec 18 23:57:36 CST 2024

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
读写Excel 支持xls、xlsx ，生成List<Map<String,Object>> 第一行表头为key

## CSV工具类  CsvUtils
读写CSV,默认分隔符为逗号。
从List<Map<String,Object>>生成CSV ，如果value有逗号，则用边界符包裹

## MinIO工具类 MinIOUtils

## Id生成器 IdGenerator
利用雪花算法生成全局唯一ID

## 日期对象 DateTime
通过构造器注入LocalDateTime来进行更加便利的日期操作
如：获得当天开始时间、当天结束时间