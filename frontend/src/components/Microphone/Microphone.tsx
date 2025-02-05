import { FaMicrophone } from 'react-icons/fa';
import { useSpeech } from '../../speech/useSpeech';
import './Microphone.css';
import { useEffect } from 'react';

export default function Microphone() {
  const {
    transcript,
    finalTranscript,
    listening,
    browserSupportsSpeechRecognition,
    startListening,
    stopListening,
    isMicrophoneAvailable,
  } = useSpeech();


  return (
    <div className="mic-container">
      <div className="mic-circle"></div>
      <FaMicrophone
        className="mic-icon"
        size={75}
        onMouseDown={() => startListening()}
        onMouseUp={() => stopListening()}
      />
    </div>
  );
}
