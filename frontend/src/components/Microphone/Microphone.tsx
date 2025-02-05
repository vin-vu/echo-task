import { FaMicrophone } from 'react-icons/fa';
import { useSpeech } from '../../hooks/useSpeech';
import './Microphone.css';
import { useEffect } from 'react';

export default function Microphone() {
  const {
    finalTranscript,
    startListening,
    stopListening,
  } = useSpeech();
  
  useEffect(() => {
    console.log('final transcript: ', finalTranscript)
  })

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
