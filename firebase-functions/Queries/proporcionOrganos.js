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
      const { organsList = [], userUId } = data[key];
      if (!organsList.length){
        return;
      }
      if (!detectionsByUser[userUId]) {
        detectionsByUser[userUId] = {}
      }
      organsList.forEach((organ) => {
        if (!detectionsByUser[userUId][organ]){
          detectionsByUser[userUId][organ] = 1;
        }else{
          detectionsByUser[userUId][organ]++;
        }
      });
      
    }));
    Object.keys(detectionsByUser).forEach((userKey) => {
      const detection = detectionsByUser[userKey];
      const organsList = Object
                            .keys(detection)
                            .reduce((row, organ) => {
                              return `${row} ${organ}: ${detection[organ]}`; 
                            },"");
      console.log(
        `Nombre Usuario: ${users[userKey].fullName}|Lista Organos: ${organsList}`);
    });
  });
});

