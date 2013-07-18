Reference Apps
===

These apps are meant to show you some of what can be done with the Bazaarvoice SDK and the best-implementation practices for doing these things. All of the code relevant to the SDK has been put in a file called _BazaarFunctions.java_ so you can find what you are looking for more easily.

Note: Using a static class to wrap these SDK calls is not a preferred practice. This is just here to make it easy to use these examples as a reference.

**Important**: If you are a Bazaarvoice client or if you are a developer representing a Bazaarvoice client, 
review submission takes at least 15 minutes to get through moderation. 
If you want to make sure your reviews are being submitted correctly, wait a bit and check https://workbench.bazaarvoice.com. 
A response with no errors does not necessarily mean that any media has been attached correctly.

Running the Apps
-
Once you have downloaded the directory of the app you want to run, follow these steps.

1. Open Eclipse and select File -> Import...
2. Under General, choose "Existing projects into workspace".
3. Next to "Select Root Directory", _Browse..._ to the root directory of the app you want to run and select Open.
4. Click _Finish_.
5. (Recommended) Attach the Javadoc for the SDK:
  - Right click on the project in the Package Explorer and choose Build Path > Configure Build Path...
  - Under the Libraries tab, drill down "bazaarandroidsdk.jar", choose "Javadac location", and click _Edit..._
	- _Browse..._ to the docs folder in the SDK root directory.
 - Click _OK_ on both windows.
6. Run the application by choosing one of the Activity files in the Package Explorer and clicking the run button.
	*	For the Intent Example, take a photo with the stock browser on a phone and share with BV Photo Share after "running" the app which installs it on the phone.

Note: The Review Submission example depends on the PhotoPicker+ project.  Both must be imported in order for the example to run.  See the Review Submission README.md for details.