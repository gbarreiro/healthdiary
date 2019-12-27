//
//  RecordsDetailViewController.swift
//  Health Diary
//
//  Created by Guille on 27/12/2019.
//  Copyright © 2019 Guillermo Barreiro. All rights reserved.
//

import UIKit

class BloodPressureDetailViewController: UITableViewController {
    
    var records: [BloodPressureReading]!

    override func viewDidLoad() {
        super.viewDidLoad()

        // Displays an Edit button in the navigation bar for this view controller.
        self.navigationItem.rightBarButtonItem = self.editButtonItem // TODO: show the edit and close buttons
        
    }

    // MARK: - Table view data source

    override func numberOfSections(in tableView: UITableView) -> Int {
        return 1 // only 1 section in the table
    }

    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return records.count // returns the number of rows the table will have
    }

    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "bloodPressureCell", for: indexPath) as! BloodPressureRecordCell
        let record = records[indexPath.row]
        
        // Fills the cell with the data
        let riskColor = BloodPressureViewController.riskColors[record.riskLevel]
        cell.systolicValue.text = String(record.systolic)
        cell.diastolicValue.text = String(record.diastolic)
        cell.recordDate.text = record.timestamp.description // TODO: use a DateFormatter
        cell.systolicValue.textColor = riskColor
        cell.diastolicValue.textColor = riskColor

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

class BloodPressureRecordCell: UITableViewCell {
    @IBOutlet weak var systolicValue: UILabel!
    @IBOutlet weak var diastolicValue: UILabel!
    @IBOutlet weak var recordDate: UILabel!
    
}
