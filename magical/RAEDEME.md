# 地图目录

```text
|-- GeoMagical [根目录]
    ├── ProjectA [项目目录]
    ├── ProjectB [项目目录]
        ├── common [公共模块]
        |   ├── data  [公共数据库]
        |       └── *.db     [数据文件]
        |   ├── log   [日志文件目录]
        |       └── *.log     [数据文件]
        |   └── layer [影像目录]
        |       ├── *.conf [地图配置]
        |       ├── *.tpk  [离线影像文件]
        |       └── *.tile [离线影像文件]
        ├── moduleA [项目业务A目录]
        ├── moduleB [项目业务B目录]
        └── moduleC [项目业务C目录]
            ├── data   [公共数据库]
            ├── photo  [照片目录]
            ├── video  [视频目录]
            ├── output [成果包导出目录]
            ├── temp    [临时文件目录]
            └── layer  [业务影像目录]
                ├── account  [业务数矢量图层目录，根据账号命名]
                |   └── *.fcs  [业务数矢量图层]
                ├── *.tpk    [离线影像目录]
                └── *.tile   [离线影像目录]  
```

# 图层配置说明

## 图层信息

```json
{
  "typeMap": {
    "tile": {
      "type": 0,
      "className": "com.geostar.map.layers.LocalTileMapLayer"
    },
    "fcs": {
      "type": 2,
      "className": "com.geostar.map.layers.FeatureMapLayer"
    }
  },
  "fileMap": {
      "basemap": {
          "name": "basemap",
          "title": "离线影像底图",
          "orderNo": 0
      },
      "卫片执法": {
          "name": "feature_weipian",
          "title": "卫片执法FCS",
          "orderNo": 1,
          "token": "GeoArtery2022.",
          "params": {
          	"key": "JCBH"
          }
      }
  }
}
```

| 字段名                 | 字段类型 | 字段说明                                        |
| ---------------------- | -------- | ----------------------------------------------- |
| typeMap                | 对象     | 文件类型映射表                                  |
| typeMap.item.type      | 整型     | 0：影像图层，1、系统图层，2业务图层             |
| typeMap.item.className | 文本     | 处理的图层类型                                  |
| fileMap                | 对象     | 文件映射表                                      |
| fileMap.item           | 对象     | 图层信息LayerInfo对象，当第一次加载的时候会读取 |
|                        |          |                                                 |
|                        |          |                                                 |

## 图层参数

| 参数名 | 参数说明 | 备注 |
| ------ | -------- | ---- |
|        |          |      |
|        |          |      |
|        |          |      |
|        |          |      |

