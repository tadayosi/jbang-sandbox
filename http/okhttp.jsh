///usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS com.squareup.okhttp3:okhttp:3.14.7

import okhttp3.*

/open PRINTING

OkHttpClient client = new OkHttpClient()
RequestBody body = RequestBody.create(MediaType.get("text/plain; charset=utf-8"), "Hello CKC!\n")
Request request = new Request.Builder().url("http://localhost:9999/test").post(body).build()
Response response = client.newCall(request).execute()
println(response)
