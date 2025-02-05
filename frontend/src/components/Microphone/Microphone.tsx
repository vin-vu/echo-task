import { FaMicrophone } from 'react-icons/fa';
import { useSpeech } from '../../hooks/useSpeech';
import './Microphone.css';

export default function Microphone() {
  const { startListening, stopListening } = useSpeech();

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
