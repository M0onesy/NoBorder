# NoBorder

一个基于 Fabric 的 Minecraft 模组项目。

## 环境要求
- JDK 21
- Gradle（可直接使用仓库内 `gradlew`）
- Minecraft 版本：1.21.6

## 本地构建
```bash
./gradlew build
```
Windows:
```powershell
.\gradlew.bat build
```

## 本地运行（开发）
```bash
./gradlew runClient
```
Windows:
```powershell
.\gradlew.bat runClient
```

## 项目结构
- `src/main/java`：核心代码与 mixin
- `src/main/resources`：`fabric.mod.json`、资源文件
- `.github/workflows/build.yml`：CI 构建流程

## 许可证
本项目采用仓库内 `LICENSE`。
