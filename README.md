# NoBorder

NoBorder 是一个基于 Fabric 的 Minecraft 模组，目标是移除原版世界边界与部分坐标限制带来的硬性约束，使玩家、实体与传送逻辑在超远坐标下仍可继续工作。

> 当前定位：实验性功能模组（偏服务端逻辑增强）

## 主要功能

- 取消 `WorldBorder` 的“是否在边界内”判定限制（多个 `contains*` 分支直接放行）。
- 取消世界边界碰撞与边界体素形状（边界不再形成物理阻挡）。
- 取消实体与玩家在部分路径中的 X/Z 坐标 `clamp`。
- 放宽 `/tp` 传送坐标有效性校验，允许传送到超出原版水平限制的位置。
- 放宽 `World.isValid(BlockPos)` 的水平位置检查。
- 在接近/超出原版外层坐标（约 `29,999,984`）时，绕过部分服务端移动包限制逻辑。

## 适用场景

- 超大地图探索、远距离坐标测试。
- 需要突破原版边界约束的服务器玩法实验。
- 与坐标边界相关的机制验证与二次开发测试。

## 兼容环境

| 项目 | 要求 |
|---|---|
| Minecraft | `1.21.6` |
| Java | `21` |
| Fabric Loader | `>=0.18.4` |
| Fabric API | `0.128.2+1.21.6` |

## 安装说明

1. 准备 Fabric 运行环境（Fabric Loader + Fabric API）。
2. 构建或获取本模组 Jar。
3. 将 Jar 放入游戏或服务器的 `mods` 目录。
4. 启动游戏/服务器并确认模组加载成功。

联机注意事项：

- 若要在多人服务器中真正突破边界限制，需要服务器安装本模组。
- 仅客户端安装不会绕过服务器端的边界与坐标校验。

## 使用与构建

本地构建：

```bash
./gradlew build
```

Windows:

```powershell
.\gradlew.bat build
```

开发环境运行客户端：

```bash
./gradlew runClient
```

Windows:

```powershell
.\gradlew.bat runClient
```

开发环境运行服务端：

```bash
./gradlew runServer
```

Windows:

```powershell
.\gradlew.bat runServer
```

构建产物位置：

- `build/libs/noborder-1.0.0.jar`
- `build/libs/noborder-1.0.0-sources.jar`

## 实现概览（简版）

- `WorldBorderMixin`：放开边界包含判定、碰撞和边界形状。
- `ServerPlayNetworkHandlerMixin`：放宽移动包/载具移动与传送相关边界限制。
- `EntityMixin`、`PlayerEntityMixin`：移除部分坐标夹取（X/Z clamp）。
- `TeleportCommandMixin`：放宽 `/tp` 调用中的世界坐标有效性检查。
- `WorldMixin`：放宽 `World.isValid(BlockPos)` 结果。

## 已知限制与风险

- 本模组不会自动优化超远坐标带来的性能开销；极远距离探索可能增加区块生成、存储和网络同步负担。
- 无配置文件开关，当前行为为“全局生效”。
- 与同类“坐标/边界/移动包”修改模组可能存在冲突。

## 项目结构

- `src/main/java`：核心逻辑与 Mixin 注入代码。
- `src/main/resources`：`fabric.mod.json`、Mixin 配置与资源文件。
- `.github/workflows/build.yml`：CI 构建流程。

## 许可证

本项目采用 **CC0-1.0**，详见 `LICENSE`。
