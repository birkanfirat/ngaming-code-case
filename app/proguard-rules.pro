# Retrofit, OkHttp, Hilt and kotlinx.serialization ship their own consumer rules,
# so only project specific entries belong here.

# Keep the generated serializers of the DTOs reached purely through reflection.
-keepclassmembers class com.ngaming.ngamingcase.**$$serializer { *; }
