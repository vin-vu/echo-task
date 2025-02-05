import { useEffect } from 'react';
import { FaMicrophone } from 'react-icons/fa';
import { useSpeech } from '../../speech/useSpeech';
import './Microphone.css';

export default function Microphone() {
  const {
    transcript,
    finalTranscript,
    listening,
    browserSupportsSpeechRecognition,
    startListening,
    stopListening,
    isMicrophoneAvailable,
    browserSupportsContinuousListening,
  } = useSpeech();

  useEffect(() => {
    console.log('transcript: ', transcript);
    console.log('final transcript: ', finalTranscript)
  });

  return (
    <div className="mic-container">
      <div className="mic-circle"></div>
      <FaMicrophone
        className="mic-icon"
        size={75}
        onMouseDown={startListening}
        onMouseUp={stopListening}
      />
    </div>
  );
}
