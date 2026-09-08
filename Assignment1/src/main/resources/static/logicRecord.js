const constraintRecord = {audio: true};
let audioRecorder;
let permittedStream;
let recordButton = document.querySelector("#recordButton");
let stopRecordButton = document.querySelector("#stopRecord");
let recordingStatus = document.querySelector("#recordingStatus");

let chunks = [];
recordButton.addEventListener("click", startRecord)
stopRecordButton.addEventListener("click", stopRecord)
async function startRecord(){
	recordingStatus.textContent = "Recording";
	chunks = [];
	permittedStream = await navigator.mediaDevices.getUserMedia(constraintRecord);
	audioRecorder = new MediaRecorder(permittedStream);
	audioRecorder.ondataavailable = (event) => { chunks.push(event.data)};
	audioRecorder.start();	
	console.log("Starting record");	
	
	audioRecorder.onstop = ()=> {
			const blobAudio = new Blob(chunks, {type: "audio/webm"});
			const url = URL.createObjectURL(blobAudio);		
			console.log(url);
			uploadAudio(blobAudio);
		};
	
}

async function stopRecord(){
	
	recordingStatus.textContent = "Stop Recording";
	console.log("Stop record");
	audioRecorder.stop();
	
	for (const track of permittedStream.getTracks()){
		track.stop();
	}
	
}

async function uploadAudio(blobAudio){
	const dataForm = new FormData();
	dataForm.append("audioRecord", blobAudio, "audioFile.webm");
	
	const postResponse = await fetch("/api/speech", { method: "POST", body: dataForm});
	return postResponse;
		
	}

