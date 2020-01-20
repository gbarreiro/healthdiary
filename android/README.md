#  Health Diary for Android

This README focuses on the particularities of the Health Diary project for Android. For information about what the app is and how it works, check the general README.

## Data storage

The blood pressure and body measures records are stored using a SQLite3 database. The records are represented with the classes BloodPressureReading and BodyMeasuresReading, and stored into and read from the DB using the HealthDatabase class, which wraps the access to the DB.

## UI

Each screen of the app is implemented through a Fragment or Activity, with their views defined as XML layouts, and their logics in the corresponding Activity/Fragment classes: MainActivity (wrapping inside the BloodPressureFragment and the BodyMeasuresFragment), BloodPressureDetailActivity and BodyMeasuresDetailActivity. 

The BloodPressureFragment and BodyMeasureFragment are shown as tabs inside the MainActivity, using a PageAdapter and a TabLayout.

## Android specific ideas for further implementation:
- Improvement of the database, using a more efficient system, like a DataBinding
