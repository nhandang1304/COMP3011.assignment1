const constraintRecord = {audio: true};
let audioRecorder;
let recordButton = document.querySelector("#recordButton");
let isRecording = false;
let chunks = [];
recordButton.addEventListener("click", ()=>{
	if (isRecording){
		isRecording = !isRecording;
		return stopRecord();
	}
	else{
		isRecording = !isRecording;
		return startRecord();
	}
})

async function startRecord(){
	const permittedStream = await navigator.mediaDevices.getUserMedia(constraintRecord);
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
	
	
	console.log("Stop record");
	audioRecorder.stop();
	
}

async function uploadAudio(blobAudio){
	const dataForm = new FormData();
	dataForm.append("audioRecord", blobAudio, "audioFile.webm");
	
	const postResponse = await fetch("/api/speech", { method: post, body: dataForm});
	
		
	}

