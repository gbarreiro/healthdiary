//
//  BodyMeasuresDetailViewController.swift
//  Health Diary
//
//  Created by Guille on 27/12/2019.
//  Copyright © 2019 Guillermo Barreiro. All rights reserved.
//

import UIKit

class BodyMeasuresDetailViewController: UITableViewController {
    
    var records: [BodyMeasureReading] = []
    let dayFormatter: DateFormatter = DateFormatter()
    let hourFormatter: DateFormatter = DateFormatter()

    override func viewDidLoad() {
        super.viewDidLoad()

        // Configures the date formatter
        dayFormatter.dateStyle = .medium
        dayFormatter.timeStyle = .none
        hourFormatter.dateStyle = .none
        hourFormatter.timeStyle = .short
        
        // Sorts the records by recent first
        records.reverse()
        
        // Displays an Edit button in the navigation bar for this view controller.
        self.navigationItem.rightBarButtonItem = self.editButtonItem
    }
    
    @IBAction func closeViewController(_ sender: Any) {
        self.dismiss(animated: true, completion: nil)
    }

    // MARK: - Table view data source
    
    override func numberOfSections(in tableView: UITableView) -> Int {
        return 1 // only 1 section in the table
    }

    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return records.count // returns the number of rows the table will have
    }

    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "bodyMeasureCell", for: indexPath) as! BodyMeasureRecordCell
        let record = records[indexPath.row]
        
        // Fills the cell with the data
        let riskColor = BodyMeasuresViewController.riskColors[record.bmiLevel]
        cell.weight.text = String(format: "%.1f", record.weight) + " kg"
        cell.height.text = String(record.height) + " cm"
        cell.bmi.text = String(format: "%.1f", record.bmi)
        cell.recordDate.text = dayFormatter.string(from: record.timestamp)
        cell.recordHour.text = hourFormatter.string(from: record.timestamp)
        cell.weight.textColor = riskColor
        cell.height.textColor = riskColor
        cell.bmi.textColor = riskColor

        return cell
    }

    override func tableView(_ tableView: UITableView, commit editingStyle: UITableViewCell.EditingStyle, forRowAt indexPath: IndexPath) {
        if editingStyle == .delete {
            // Delete the row from the data source
            self.records.remove(at: indexPath.row) // TODO: remove from the actual datasource
            tableView.deleteRows(at: [indexPath], with: .fade)
        }
    }

}

class BodyMeasureRecordCell: UITableViewCell {
    @IBOutlet weak var weight: UILabel!
    @IBOutlet weak var height: UILabel!
    @IBOutlet weak var bmi: UILabel!
    @IBOutlet weak var recordDate: UILabel!
    @IBOutlet weak var recordHour: UILabel!
    
}
