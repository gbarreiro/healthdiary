//
//  RecordsDetailViewController.swift
//  Health Diary
//
//  Created by Guille on 27/12/2019.
//  Copyright © 2019 Guillermo Barreiro. All rights reserved.
//

import UIKit
import Charts
import CoreData

/**
    Detail View Controller for Blood Pressure.
    Shows a list with all the stored Blood Pressure records, showing the systolic and diastolic values, as well as the date when it was registered.
 
    This View Controller reads the records from a NSFetchedResultsController, component which updates the UITableView every time a new record is added or deleted.
 */
class BloodPressureDetailViewController: UITableViewController, NSFetchedResultsControllerDelegate {
        
    // MARK: Charts
    @IBOutlet weak var chartView: LineChartView!
    var diastolicEntries = [ChartDataEntry]()
    var systolicEntries = [ChartDataEntry]()
    
    private func addRecordToChart(record: BloodPressureReading) {
        diastolicEntries.append(ChartDataEntry(x: Double(diastolicEntries.count+1), y: Double(record.diastolic)))
        systolicEntries.append(ChartDataEntry(x: Double(systolicEntries.count+1), y: Double(record.systolic)))
        chartView.notifyDataSetChanged()
    }
    
    private func deleteRecordFromChart(index: Int){
        diastolicEntries.remove(at: index)
        systolicEntries.remove(at: index)
        chartView.notifyDataSetChanged()
    }
    
    private func initializateChart(){

        
        
        // Fill ChartData entries
        if let numberOfElements = fetchedResultsController.sections?.first!.numberOfObjects{
            if numberOfElements > 0 {
                for record in fetchedResultsController.fetchedObjects!.reversed() {
                    addRecordToChart(record: record)
                }
                
                // Create chart lines
                let diastolicLine = LineChartDataSet(entries: diastolicEntries, label: "Diastolic")
                let systolicLine = LineChartDataSet(entries: systolicEntries, label: "Systolic")
                
                // Customization for chart lines: diastolic
                diastolicLine.colors = [UIColor.blue]
                diastolicLine.circleColors = [UIColor.blue]
                diastolicLine.lineWidth = 2
                diastolicLine.drawCircleHoleEnabled = false
                diastolicLine.drawValuesEnabled = false
                
                // Customization for chart lines: systolic
                systolicLine.colors = [UIColor.red]
                systolicLine.circleColors = [UIColor.red]
                systolicLine.lineWidth = 2
                systolicLine.drawCircleHoleEnabled = false
                systolicLine.drawValuesEnabled = false

                // Links the lines to the chart
                let chartData = LineChartData()
                chartData.addDataSet(diastolicLine)
                chartData.addDataSet(systolicLine)
                chartView.data = chartData
            }
        }
        // Customize the chart
        chartView.noDataText = "No records"
        chartView.setScaleEnabled(true)
        chartView.xAxis.enabled = false
        chartView.animate(xAxisDuration: 1.5, yAxisDuration: 0.0)
        chartView.doubleTapToZoomEnabled = false
        chartView.highlightPerTapEnabled = false
        chartView.highlightPerDragEnabled = false
        chartView.dragEnabled = true
        chartView.pinchZoomEnabled = false
        chartView.setVisibleXRange(minXRange: 5, maxXRange: 5)
        chartView.moveViewToX(Double(diastolicEntries.count))

        
        
    }
    
    // MARK: Data sources
    private var fetchedResultsController: NSFetchedResultsController<BloodPressureReading>!
    
    // The NSManagedObjectContext needed for creating and deleting objects is accessed through the shared singleton in `DataController.shared.viewContext`
    
    private func setupFetchedResultsController() {
        if fetchedResultsController == nil { // avoids creating a new NSFetchedResultsController if one already exists
            let fetchRequest:NSFetchRequest<BloodPressureReading> = BloodPressureReading.fetchRequest()
            let sortDescriptor = NSSortDescriptor(key: "timestamp", ascending: false) // shows the readings from newest to oldest
            fetchRequest.sortDescriptors = [sortDescriptor]
            
            fetchedResultsController = NSFetchedResultsController(fetchRequest: fetchRequest, managedObjectContext: DataController.shared.viewContext, sectionNameKeyPath: nil, cacheName: "bloodpressure")
            fetchedResultsController.delegate = self
            
            do {
                try fetchedResultsController.performFetch()
            } catch {
                fatalError("The fetch could not be performed: \(error.localizedDescription)")
            }
        }

    }

    // MARK: UIViewController lifecycle
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Displays an Edit button in the navigation bar for this view controller.
        self.navigationItem.rightBarButtonItem = self.editButtonItem
        
        // Set up FetchedResultsController
        setupFetchedResultsController()
        
        initializateChart()
        
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        
        // Set up FetchedResultsController
        setupFetchedResultsController()
        
        if let indexPath = tableView.indexPathForSelectedRow {
            tableView.deselectRow(at: indexPath, animated: false)
            tableView.reloadRows(at: [indexPath], with: .fade)
        }
    }
    
    override func viewDidDisappear(_ animated: Bool) {
        super.viewDidDisappear(animated)
        
        // Release FetchedResultsViewController
        fetchedResultsController = nil
    }

    @IBAction func closeViewController(_ sender: Any) {
        // Dismiss the list and goes back to the MainVC
        self.dismiss(animated: true, completion: nil)
    }
    
    // MARK: - Table view data source

    override func numberOfSections(in tableView: UITableView) -> Int {
        return fetchedResultsController.sections?.count ?? 1
    }

    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return fetchedResultsController.sections?[section].numberOfObjects ?? 0
    }

    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        // Creates a cell and fills it with the blood pressure record data
        let cell = tableView.dequeueReusableCell(withIdentifier: "bloodPressureCell", for: indexPath) as! BloodPressureRecordCell
        let record = fetchedResultsController.object(at: indexPath)
        cell.loadReading(record: record)

        return cell
    }

    override func tableView(_ tableView: UITableView, commit editingStyle: UITableViewCell.EditingStyle, forRowAt indexPath: IndexPath) {
        if editingStyle == .delete {
            // Delete the item from the data source
            let deletedReading = fetchedResultsController.object(at: indexPath)
            DataController.shared.viewContext.delete(deletedReading)
            try? DataController.shared.viewContext.save()
        }
    }
    
    func controller(_ controller: NSFetchedResultsController<NSFetchRequestResult>, didChange anObject: Any, at indexPath: IndexPath?, for type: NSFetchedResultsChangeType, newIndexPath: IndexPath?) {
        // A reading has been added or removed
        switch type {
        case .insert:
            tableView.insertRows(at: [newIndexPath!], with: .fade) // updates table
            addRecordToChart(record: fetchedResultsController.object(at: newIndexPath!)) // updates chart
            break
        case .delete:
            tableView.deleteRows(at: [indexPath!], with: .fade) // updates table
            deleteRecordFromChart(index: indexPath!.row) // updates chart
            break
        default:
            // Do nothing, not allowed ops
            break
        }
    }
    
    
    func controllerWillChangeContent(_ controller: NSFetchedResultsController<NSFetchRequestResult>) {
        tableView.beginUpdates()
    }
    
    func controllerDidChangeContent(_ controller: NSFetchedResultsController<NSFetchRequestResult>) {
        tableView.endUpdates()
    }

}
