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
      const { organsList, description = "", doctorId, userUId} = data[key];
      if (!doctorId){
        return;
      }
      const organs = organsList ? organsList.join(): "No selecciono organos";
      console.log(
        `NombreUsuario: ${users[userUId].fullName}|NombreDoctor: ${users[doctorId].fullName}|description por doctor: ${description}\n|Lista de organos: ${organs}`);
    }));
  });
});

