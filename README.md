### iToys-Core

```
.
├── simple             App module.
│     └── keystore      签名文件 folder.
├── iToys-core          iToys Core.
│     ├── common        公共 module.
│     ├── download      下载 module.
│     ├── hybrid        网页 module.
│     ├── image         图片 module.
│     ├── logcat        日志 module.
│     ├── network       网络 module.
│     ├── pay           支付 module.
│     ├── permission    权限 module.
│     ├── scanning      扫描 module.
│     ├── share         分享 module.
│     ├── uikit         UI module.
│     ├── utils         工具类 module.
│     └── versions      版本控制 module.
└── iToys-plugin        iToys 插件.
```

### 按钮(Button)

按钮统一使用`com.google.android.material.button.MaterialButton`.

按钮样式：
- @style/IToysAndroid.Button.Background
- @style/IToysAndroid.Button.BackgroundTint

#### @style/IToysAndroid.Button.Background

通过设置`android:background`属性设置按钮背景，可使用`drawable`、`shape`等, 一般用来设置渐变色按钮.

```xml
<com.google.android.material.button.MaterialButton 
    style="@style/IToysAndroid.Button.Background"
    android:background="@drawable/enterprise_renewal_variant_gradient" 
    android:id="@+id/renewal"
    android:layout_height="30dp" 
    android:layout_width="80dp"
    android:text="@string/enterprise_renewal" 
    app:layout_constraintBottom_toBottomOf="parent"
    app:layout_constraintEnd_toEndOf="parent" 
    app:layout_constraintTop_toTopOf="parent" />
```

#### @style/IToysAndroid.Button.BackgroundTint

通过设置`app:backgroundTint`属性设置按钮背景颜色，只能使用`color`, 用来设置纯色按钮.
通过设置`app:cornerRadius`属性设置按钮圆角大小.

```xml
<com.google.android.material.button.MaterialButton
    android:id="@+id/package_increment"
    style="@style/IToysAndroid.Button.BackgroundTint"
    android:layout_width="56dp"
    android:layout_height="28dp"
    android:text="@string/enterprise_package_increment"
    android:textSize="12sp"
    app:backgroundTint="@color/uikit_colorful_54DD81"
    app:cornerRadius="8dp"
    app:layout_constraintBottom_toBottomOf="parent"
    app:layout_constraintEnd_toEndOf="parent"
    app:layout_constraintTop_toTopOf="parent" />
```

### 文件上传(Upload)

* 引用`network` Module.

```groovy
implementation(projects.iToysCore.network)
```

* 通过`Hilt`依赖注入:
```kotlin
/**
* 提供 七牛api
*/
@Provides
@Singleton
fun provideQiNiuApi() = QiNiuApi::class.java.toApiService()

/**
 * 提供 七牛 Upload Repository
 */
@Provides
@Singleton
fun provideQiNiuRepository() = QiNiuRepository(provideQiNiuApi())
```

* 在`ViewModel`中使用：
```kotlin
@HiltViewModel
class HousingInformationViewModel @Inject constructor(
    private val qiNiuRepository: QiNiuRepository,
) : AbsViewModel<HousingInformationIntent, HousingInformationState>()


// 上传
launchOnIO {
    qiNiuRepository.uploadFile(
        File(intent.picture), // 文件
        Category.File,    // 文件类型 
        TokenType.Public, // 上传文件权限范围, Public(公开) or Private(私有)
        "test", // 可以为空字符串
        success = { logcat { "图片地址 -> $it" } }, // 上传成功回调, Public(公开): 完整的url地址, Private(私有): 上传的文件路径
        handleEx = {}, // 上传失败回调
    )
}
```