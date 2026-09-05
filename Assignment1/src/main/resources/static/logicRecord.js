const constraintRecord = {audio: true};
let audioRecorder;
let recordButton = document.querySelector("#recordButton");
let chunks = [];
async function startRecord(){
	const permittedStream = await navigator.mediaDevices.getUserMedia(constraintRecord);
	audioRecorder = new MediaRecorder(permittedStream);
	audioRecorder.ondataavailable = (event) => { chunks.push(event.data)};
	audioRecorder.start();	
	
}
