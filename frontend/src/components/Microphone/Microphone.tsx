import { FaMicrophone } from 'react-icons/fa';
import { IntentResponse, useSpeech } from '../../hooks/useSpeech';
import './Microphone.css';

interface MicrophoneProps {
  editTasksHandler: (intentPayload: IntentResponse) => void;
}
export default function Microphone({ editTasksHandler}: MicrophoneProps) {
  const { startListening, stopListening } = useSpeech(editTasksHandler);

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
