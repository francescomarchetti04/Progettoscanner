# software da installare su pc

## Schema generale e suddivisione package

```md
YGOScannerPC/
│
├── MainApp.java
│
├── ui/
│   ├── DashboardController.java
│   └── MainView.fxml
│
├── core/
│   ├── SyncManager.java
│   ├── JsonImporter.java
│   └── FileWatcherService.java
│
├── database/
│   ├── DatabaseManager.java
│   └── CardDao.java
│
├── model/
│   ├── CardResult.java
│   └── ScanSession.java
│
└── util/
    └── ConfigManager.java
 ```

## come rendere i package un eseguibile portable