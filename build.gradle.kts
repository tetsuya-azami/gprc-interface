import com.google.protobuf.gradle.id

plugins {
    kotlin("jvm") version "1.9.22"
    id("com.google.protobuf") version "0.9.4"
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

val grpcVersion = "1.60.1"
val protobufVersion = "3.25.3"
val grpcKotlinVersion = "1.3.0"


dependencies{
    // protoファイル内でgoogle/protobuf/配下のパッケージを使用するために必要(etc google.protobuf.Timestamp)
    implementation("com.google.protobuf:protobuf-java:${protobufVersion}")
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:${protobufVersion}"
    }
    plugins {
        id("grpc") {
            // こちらを追加しないとgrpc(java)のコードが生成されない
            artifact = "io.grpc:protoc-gen-grpc-java:${grpcVersion}"
        }
        id("grpckt") {
            // こちらを追加しないとgrpc-kotlinのコードが生成されない
            artifact = "io.grpc:protoc-gen-grpc-kotlin:${grpcKotlinVersion}:jdk8@jar"
        }
    }
    generateProtoTasks {
        all().forEach {
            it.plugins {
                // こちらを追加しないとgrpcのコードが生成されない
                id("grpc")
                id("grpckt")
            }
            it.builtins {
                // protoファイルからkotlinのコードを生成する
                create("kotlin")
            }
        }
    }
}
