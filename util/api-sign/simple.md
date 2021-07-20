# 简单签名说明 #

简单签名只对参数进行签名计算

## 签名算法

  签名由拼接的参数加上clientSecret值进行MD5加密组成.

* 筛选
    获取所有请求参数，不包括字节类型参数，如文件、字节流，不包括签名参数“sign”。

* 排序
    将筛选的参数按照参数名第一个字符的键值ASCII码递增排序（字母升序排序），如果遇到相同字符则按照第二个字符的键值ASCII码递增排序，以此类推。如果遇到一个参数多个值（如：q=1&q=2）则转换为一个值为英文逗号（,）分隔的参数（如：q=1,2）
    
* 拼接
    将排序后的参数与其对应值，组合成“参数=参数值”的格式,加上clientSecret值，并且把这些参数用&字符连接起来。待签名字符串为"UTF-8"编码。此时生成的字符串为待签名字符串。
    例如下面的请求示例，参数值都是示例，开发者参考格式即可：
    
```
      REQUEST URL: https://www.xxx.com?page=0&size=25&type=0&showTime=&start=0&limit=25
      假如clientSecret为:alkdfjaso
      组成的待签名字符串为：
      
        limit=25&page=0&size=25&start=0&type=0&alkdfjaso
      
```
     
    
> 注意：
         
>> 没有值的参数无需传递，也无需包含到待签名数据中；
         
>> 待签名数据应该是原生值而不是URL Encoding之后的值。例如：调用某接口需要对请求参数email进行数字签名，那么待签名数据应该是email=test@msn.com，而不是email=test%40msn.com。

* 使用MD5对待签名字符串进行加密码。
    
```html
      
      待签名字符串为：limit=25&page=0&size=25&start=0&type=0&alkdfjaso
      
      加密后值：26d595f645505e93a50600c1d72ba09d

```