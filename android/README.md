#  Health Diary for Android

This README focuses on the particularities of the Health Diary project for Android. For information about what the app is and how it works, check the general README.

## Data storage

The blood pressure and body measures records are stored using the Android Room library, which is a wrapper for a SQLite3 database. 
The records are represented with the data classes (entities) BloodPressureReading and BodyMeasuresReading.
The CRUD operations are done through the DAO interfaces, which wrap the SQL queries into public methods.
The database itself is a singleton object, accessible through the HealthDatabase classs.

## UI

Each screen of the app is implemented through a Fragment or Activity, with their views defined as XML layouts, and their logics in the corresponding Activity/Fragment classes: 
- MainActivity: TabActivity
    - BloodPressureFragment: registring new blood pressure records and showing the average values
    - BodyMeasuresFragment: registring new body measures records and showing the average values
- BloodPressureDetailActivity: list with the blood pressure records
- BodyMeasuresDetailActivity: list with the body measures records

The BloodPressureFragment and BodyMeasureFragment are shown as tabs inside the MainActivity, using a PageAdapter and a TabLayout.

## Android specific ideas for further implementation:
- Use a ViewModel
