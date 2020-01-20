#  Health Diary for iOS

This README focuses on the particularities of the Health Diary project for iOS. For information about what the app is and how it works, check the general README.

## Data storage

The blood pressure and body measures records are stored using Apple's Core Data framework. The two types of readings are defined as Entities in the data model, and the access to them is done using a NSFetchedResultsController, implemented on the corresponding ViewController classes.

## UI

Each screen of the app is implemented through a ViewController, with their views defined in a Storyboard, and their logics in the corresponding ViewController classes: BloodPressureMainViewController, BodyMeasuresMainViewController, BloodPressureDetailViewController and BodyMeasuresDetailViewController. 

For showing some "Next" and "Register" buttons in the numeric pad when entering the blood pressure or body measures, I had to create an extension for the UITextField. I really don't understand why this is the only way to accomplish it, and Apple doesn't offer a more straight-forward solution.

## iOS specific ideas for further implementation:
- Integration with the native Health app
