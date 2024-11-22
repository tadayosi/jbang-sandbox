OS ?= osx
ARCH ?= aarch_64

grpc-protoc-hello:
	protoc --java_out=./grpc ./grpc/hello.proto
	protoc \
		--plugin=protoc-gen-grpc-java=./protoc-gen-grpc-java-1.68.1-$(OS)-$(ARCH).exe \
		--grpc-java_out=./grpc \
		./grpc/hello.proto
