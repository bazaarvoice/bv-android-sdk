ReviewSubmissionExample
====================

This app simulates the flow of submitting a review for a product. The first screen is a product information page giving you the option to rate the product. Then you can take a picture for your review, and input your title, nickname, and review text. The submission uses client-side validation before and server-side validation after submitting to make sure that no fields are insufficient. If the submission is completed, your review is shown.

Note: This app illustrates submission using the chute photo picker.  In order for it to compile, you must also import the (included) PhotoPicker+ project to your Eclipse workspace.  Then, once you have imported both projects:

1. Highlight the review submission project in the Eclipse Package Explorer.
2. Navigate to Project->Properties.
3. In the properties dialog, select "Android" on the left.
4. In the Library pane, ensure that PhotoPicker+ is referenced.  If not, Click "add" and add the PhotoPicker+ project as a reference.
