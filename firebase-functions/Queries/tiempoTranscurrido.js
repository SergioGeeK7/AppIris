const admin = require("firebase-admin");
const serviceAccount = require("./appiris-31956-firebase-adminsdk-senn5-6301316e35.json");

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
  databaseURL: "https://appiris-31956.firebaseio.com"
});

const detectionRef = admin.database().ref("detections");
const usersRef = admin.database().ref("users");

usersRef.on('value', (snapshot) => {
  const users = snapshot.val();
  detectionRef.on('value', (snapshot) => {
    const data = snapshot.val();
    Object.keys(data).forEach(((key) => {
      const { date, analyzed, doctorId, userUId } = data[key];
      const requested = new Date(date.time);
      const analyzedTime = new Date(analyzed && analyzed.time);
      const timeElapsed = analyzed ? timeConversion(analyzedTime - requested) : "No analizado";
      const doctorName = users[doctorId] ? `|NombreDoctor: ${users[doctorId].fullName}`:"";

      console.log(
        `NombreUsuario: ${users[userUId].fullName}${doctorName}|TiempoTranscurrido: ${timeElapsed}`);
    }));
  });
});


function timeConversion(millisec) {
  var seconds = (millisec / 1000).toFixed(1);
  var minutes = (millisec / (1000 * 60)).toFixed(1);
  var hours = (millisec / (1000 * 60 * 60)).toFixed(1);
  var days = (millisec / (1000 * 60 * 60 * 24)).toFixed(1);
  if (seconds < 60) {
    return seconds + " Sec";
  } else if (minutes < 60) {
    return minutes + " Min";
  } else if (hours < 24) {
    return hours + " Hrs";
  } else {
    return days + " Days"
  }
}