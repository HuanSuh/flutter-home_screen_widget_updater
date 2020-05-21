//
//  TodayViewController.swift
//  widget
//
//  Created by Huan Suh on 2020/05/20.
//  Copyright © 2020 The Chromium Authors. All rights reserved.
//

import UIKit
import NotificationCenter

class TodayViewController: UIViewController, NCWidgetProviding {
        
    @IBOutlet var widgetText: UILabel!
    
    private let kAppGroupName = "group.widgetupdater"
    private var sharedContainer : UserDefaults?
    var dataDict: [String:Any]?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        self.reloadInputViews()
        self.sharedContainer = UserDefaults(suiteName: kAppGroupName)
        //self.widgetText.reloadInputViews()
    }
        
    func widgetPerformUpdate(completionHandler: (@escaping (NCUpdateResult) -> Void)) {
        // Perform any setup necessary in order to update the view.
        
        // If an error is encountered, use NCUpdateResult.Failed
        // If there's no update required, use NCUpdateResult.NoData
        // If there's an update, use NCUpdateResult.NewData
        fetchDataFromSharedContainer()
        completionHandler(NCUpdateResult.newData)
    }
    
    fileprivate func fetchDataFromSharedContainer()
    {
        print("log:::::")
        if let sharedContainer = self.sharedContainer, let dataDict = sharedContainer.dictionary(forKey: "HOME_SCREEN_WIDGET_DATA_KEY")
        {
            print("log::::::::::::")
            self.dataDict = dataDict

            let format = DateFormatter()
            format.locale = Locale(identifier: "ko_kr")
            format.dateFormat = "yyyy-MM-dd hh:mm:ss"
            if let data = self.dataDict?["data"] as! String? {
                widgetText.text = data
            } else {
                widgetText.text = format.string(from: Date())
            }
        }
    }
}
