import Foundation
import WebKit

public struct PrintOptions {
    var monochrome: Bool
    var orientation: UIPrintInfo.Orientation
}

@objc public class NativePrint: NSObject {
    public typealias Handler = (Result<Bool, Error>) -> Void
    
    public func print(webView: WKWebView, name: String, printOptions: PrintOptions, completionHandler: @escaping Handler) {
        let webViewPrint = webView.viewPrintFormatter()
        
        let printInfo = UIPrintInfo(dictionary: nil)
        printInfo.jobName = name
        printInfo.outputType = if printOptions.monochrome { .grayscale } else { .general }
        printInfo.orientation = printOptions.orientation
        
        let printController = UIPrintInteractionController.shared
        printController.printInfo = printInfo
        printController.printFormatter = webViewPrint
        
        DispatchQueue.main.async {
            printController.present(animated: true) { UIPrintInteractionController, printed, error in
                if (error != nil) {
                    completionHandler(.failure(error!))
                } else {
                    completionHandler(.success(printed))
                }
            }
        }
    }
}
