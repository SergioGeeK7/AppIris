const admin = require("firebase-admin");
const serviceAccount = require("./appiris-31956-firebase-adminsdk-senn5-6301316e35.json");

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
  databaseURL: "https://appiris-31956.firebaseio.com"
});

const detectionRef = admin.database().ref("detections");
const usersRef = admin.database().ref("users");
const detectionsByUser = {};

usersRef.on('value', (snapshot) => {
  const users = snapshot.val();
  detectionRef.on('value', (snapshot) => {
    const data = snapshot.val();
    Object.keys(data).forEach(((key) => {
      const { date, userUId } = data[key];
      const dateCreated = new Date(date.time).toLocaleString();
      if (!detectionsByUser[userUId]){
        detectionsByUser[userUId] = [];
      }
      detectionsByUser[userUId].push(dateCreated);
    }));
    Object.keys(detectionsByUser).forEach((userKey)=>{
      console.log(
        `Nombre Usuario: ${users[userKey].fullName}\nFechas Capturas: ${detectionsByUser[userKey].join("\n")}\nNumero Capturas: ${detectionsByUser[userKey].length}`);
    });
  });
});
