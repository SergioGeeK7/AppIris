
// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
// exports.helloWorld = functions.https.onRequest((request, response) => {
//  response.send("Hello from Firebase!");
// });
//import firebase functions modules
const functions = require('firebase-functions');
//import admin module
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

// Listens for new messages added to messages/:pushId
exports.pushNotification = functions.database.ref('/detections/{pushId}/state').onWrite(event => {

  console.log('Push notification event triggered');

  //  Grab the current value of what was written to the Realtime Database.
  const DOCTOR_TOPIC = "DOCTOR_TOPIC";
  const data = event.data.val();
  const detectionId = event.params.pushId;

  if (data === "pending") {
    const payload = {
      notification: {
        title: "You have a new patient",
        body: "Open up the application to see the analysis",
        sound: "default"
      }
    };
    const options = {
      priority: "high",
      timeToLive: 60 * 60 * 24
    };
    return admin.messaging().sendToTopic(DOCTOR_TOPIC, payload, options);
  }

  if (data === "done") {
    admin.database()
      .ref(`/detections/${detectionId}/messagingToken`)
      .once('value')
      .then((snapMessagingToken) => {

        const payload = {
          notification: {
            title: "Your results are ready",
            body: "Open up the application to see your results ",
            sound: "default"
          }
        };
        return admin.messaging().sendToDevice(snapMessagingToken.val(), payload);
      });
  }
});
