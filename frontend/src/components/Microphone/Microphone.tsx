import { FaMicrophone } from 'react-icons/fa';
import { useSpeech } from '../../hooks/useSpeech';
import { IntentResponse } from '../../api/Api';
import './Microphone.css';

interface MicrophoneProps {
  handleVoiceCommands: (intentPayload: IntentResponse) => void;
}
export default function Microphone({
  handleVoiceCommands: handleVoiceCommands,
}: MicrophoneProps) {
  const { startListening, stopListening } = useSpeech(handleVoiceCommands);

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
