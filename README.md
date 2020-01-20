#  Health Diary app

App for Android and iOS for logging the blood pressure and body measures of a person. The app is divided in two sections: one for the blood pressure, and other for the body measures. From the blood pressure one, the user can insert his last systolic and diastolic values, or insert some gaussian randomly generated ones, and then see the average values, the risk level (represented as a color), as well as a list with all the records, sorted by date. The same applies for the body measures, this is, the weight and height of the user.

In the README for each OS (Android and iOS), there is more info about the app for each specific platform.

## Types of records
- Blood pressure: systolic value (mmHg), diastolic value (mmHg) and date.
- Body measures: weight (kg), height (cm) and date.

## Navigation
The app structure has two screens for each type of measure:
- Main: data input and average values and risk displaying
- Detail: list with all the records

## Ideas for further implementation:
- Graphs: show a graph with the last inserted values in the blood pressure and body measures detail screens
- Testing: implement some basic testing cases
- Widget: a widget showing the average values
- Companion app for watchOS / Android Wear

Ideas specific for only one platform are detailed in their corresponding READMEs
