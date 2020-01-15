//
//  BloodPressureCell.swift
//  Health Diary
//
//  Created by Guille on 06/01/2020.
//  Copyright © 2020 Guillermo Barreiro. All rights reserved.
//

import Foundation
import UIKit

/**
    Table cell for displaying a Blood Pressure record.
 */
class BloodPressureRecordCell: UITableViewCell {
    @IBOutlet weak var systolicValue: UILabel!
    @IBOutlet weak var diastolicValue: UILabel!
    @IBOutlet weak var recordDate: UILabel!
    @IBOutlet weak var recordHour: UILabel!
    
    /**
        Fill the cell with the data from the Blood Pressure reading
     */
    func loadReading(record: BloodPressureReading){
        let riskColor = RiskColors.bloodPressureRiskColors[record.riskLevel]
        self.systolicValue.text = String(record.systolic) + " mmHg"
        self.diastolicValue.text = String(record.diastolic) + " mmHg"
        self.recordDate.text = dayFormatter.string(from: record.timestamp!)
        self.recordHour.text = hourFormatter.string(from: record.timestamp!)
        self.systolicValue.textColor = riskColor
        self.diastolicValue.textColor = riskColor
    }
}
