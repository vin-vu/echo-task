import { FaMicrophone } from 'react-icons/fa';
import { useSpeech } from '../../hooks/useSpeech';
import { IntentResponse } from '../../api/Api';
import { Dispatch, SetStateAction } from 'react';
import './Microphone.css';

interface MicrophoneProps {
  handleVoiceCommands: (intentPayload: IntentResponse) => void;
  setTranscript: Dispatch<SetStateAction<string>>;
}
export default function Microphone({
  handleVoiceCommands,
  setTranscript,
}: MicrophoneProps) {
  const { startListening, stopListening } = useSpeech({
    handleVoiceCommands,
    setTranscript,
  });

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
