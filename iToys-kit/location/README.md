#### Location(定位)

+ 添加配置
在App module `AndroidManifest.xml`中添加:
```xml
<!-- 高德地图API -->
<meta-data
    android:name="com.amap.api.v2.apikey"
    android:value="${aMap}" />
```

+ 引用
```groovy
implementation(projects.iToysCore.location)
```