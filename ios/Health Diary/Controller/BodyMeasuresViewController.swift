//
//  BodyMeasuresViewController.swift
//  Health Diary
//
//  Created by Guille on 10/12/2019.
//  Copyright © 2019 Guillermo Barreiro. All rights reserved.
//

import UIKit
import CoreData

class BodyMeasuresViewController: UIViewController, UITextFieldDelegate, NSFetchedResultsControllerDelegate {
    
    // Colors for the BMI values, depending on the risk level
    public static let riskColors: [BodyMeasureReading.BMILevel: UIColor] = [
        BodyMeasureReading.BMILevel.HEALTHY: UIColor.green,
        BodyMeasureReading.BMILevel.UNDERWEIGHT: UIColor.orange,
        BodyMeasureReading.BMILevel.OVERWEIGHT: UIColor.orange,
        BodyMeasureReading.BMILevel.OBESE: UIColor.red
    ]
    
    // MARK: UIOutlets
    @IBOutlet weak var weightInput: UITextField!
    @IBOutlet weak var heightInput: UITextField!
    @IBOutlet weak var weightAverage: UILabel!
    @IBOutlet weak var heightAverage: UILabel!
    @IBOutlet weak var bmiAverage: UILabel!
    @IBOutlet weak var registerButton: UIButton!
    @IBOutlet weak var randomSwitch: UISwitch!
    
    // MARK: Data sources
    private var fetchedResultsController: NSFetchedResultsController<BodyMeasureReading>!
    
    private func setupFetchedResultsController() {
        if fetchedResultsController == nil {
            let fetchRequest:NSFetchRequest<BodyMeasureReading> = BodyMeasureReading.fetchRequest()
            let sortDescriptor = NSSortDescriptor(key: "timestamp", ascending: false) // shows the readings from newest to oldest
            fetchRequest.sortDescriptors = [sortDescriptor]
            
            fetchedResultsController = NSFetchedResultsController(fetchRequest: fetchRequest, managedObjectContext: DataController.shared.viewContext, sectionNameKeyPath: nil, cacheName: "bodymeasure")
            fetchedResultsController.delegate = self
            
            do {
                try fetchedResultsController.performFetch()
            } catch {
                fatalError("The fetch could not be performed: \(error.localizedDescription)")
            }
        }

    }
    
    // MARK: UIActions
    @IBAction func updateUIStatus() {
        if(randomSwitch.isOn){
            // With the random switch on, text inputs are always disabled and the register button is always enabled
            weightInput.isEnabled = false
            heightInput.isEnabled = false
            registerButton.isEnabled = true
        
        }else{
            // With the random switch off, text inputs are always enabled
            weightInput.isEnabled = true
            heightInput.isEnabled = true
            
            // With the random switch off, the register button will only be enabled if both text inputs aren't empty
            registerButton.isEnabled = (!(weightInput.text!.isEmpty) && !(heightInput.text!.isEmpty))
        }
        
    }

    
    @IBAction func registerValues() {
        var reading: BodyMeasureReading?
        
        if(randomSwitch.isOn){
            // Random values for the body measure reading
            reading = BodyMeasureReading(context: DataController.shared.viewContext, timestamp: Date())
            print("Random value\nWeight: \(String(describing: reading!.weight)). Height: \(String(describing: reading!.height))")
        } else{
            // User introduced values for the body measure reading
            if let weight = Float(weightInput.text!), let height = Int(heightInput.text!){
                if weight > 0 && height > 0 {
                    // Introduced weight and height values are positive integers
                    reading = BodyMeasureReading(context: DataController.shared.viewContext, weight: weight, height: height, timestamp: Date())
                    print("Inserted value\nWeight: \(String(describing: reading!.weight)). Height: \(String(describing: reading!.height))")
                }
            }
            
            // Clears the text inputs
            weightInput.text = ""
            heightInput.text = ""
        }
        
        // Registers the reading in the array
        if reading != nil{
            try? DataController.shared.viewContext.save()
        }

    }
    
    // Avoids the typing of non-digit characters in the input text fields and of too big numbers
    func textField(_ textField: UITextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool {
        
        // Handle backspace/delete
        guard !string.isEmpty else {
            // Backspace detected, allow text change, no need to process the text any further
            return true
        }
        
        if textField == self.heightInput {
            // Only 3 digits allowed, no decimal points
            return string.rangeOfCharacter(from: CharacterSet.decimalDigits) != nil && textField.text!.count < 3
        } else if textField == self.weightInput {
            return string.rangeOfCharacter(from: CharacterSet.decimalDigits.union(CharacterSet(charactersIn: ".,") )) != nil && textField.text!.count < 4
        } else {
            return false
        }
        
    }
    
    // MARK: ViewController lifecycle methods
    override func viewDidLoad() {
        super.viewDidLoad()
        setupFetchedResultsController()
        updateAverageValues()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        
        // Set up FetchedResultsController
        setupFetchedResultsController()
    }
    
    override func viewDidDisappear(_ animated: Bool) {
        super.viewDidDisappear(animated)
        
        // Release FetchedResultsViewController
        self.fetchedResultsController = nil
    }
    
    private func updateAverageValues() {
        // Updates the average values
        let average = BodyMeasureReading.calculateAverage(records: fetchedResultsController.fetchedObjects ?? [])
        if let average = average {
            let riskColor = BodyMeasuresViewController.riskColors[average.riskLevel]
            weightAverage.text = String(format: "%.1f", average.weight)
            heightAverage.text = String(average.height)
            bmiAverage.text = String(format: "%.1f", average.bmi)
            weightAverage.textColor = riskColor
            heightAverage.textColor = riskColor
            bmiAverage.textColor = riskColor
        } else {
            // No readings registered
            weightAverage.text = "–"
            heightAverage.text = "–"
            bmiAverage.text = "–"
            weightAverage.textColor = UIColor.purple
            heightAverage.textColor = UIColor.purple
            bmiAverage.textColor = UIColor.purple
            
        }

    }
    
    func controller(_ controller: NSFetchedResultsController<NSFetchRequestResult>, didChange anObject: Any, at indexPath: IndexPath?, for type: NSFetchedResultsChangeType, newIndexPath: IndexPath?) {
        // Reading added or deleted
        updateAverageValues()
    }
        
}
