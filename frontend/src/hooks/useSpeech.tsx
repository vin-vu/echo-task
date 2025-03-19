import SpeechRecognition, {
  useSpeechRecognition,
} from 'react-speech-recognition';
import { Dispatch, SetStateAction, useEffect } from 'react';
import { fetchIntent, IntentResponse } from '../api/Api';

interface useSpeechProps {
  handleVoiceCommands: (intentPayload: IntentResponse) => void;
  setTranscript: Dispatch<SetStateAction<string>>;
}

export const useSpeech = ({
  handleVoiceCommands,
  setTranscript,
}: useSpeechProps) => {
  const {
    transcript,
    finalTranscript,
    listening,
    browserSupportsSpeechRecognition,
    isMicrophoneAvailable,
  } = useSpeechRecognition();

  const startListening = SpeechRecognition.startListening;
  const stopListening = SpeechRecognition.stopListening;

  useEffect(() => {
    const updateTaskList = async () => {
      const intentPayload = await fetchIntent(finalTranscript);
      if (intentPayload) {
        handleVoiceCommands(intentPayload);
      }
    };
    updateTaskList();
    setTranscript(finalTranscript);
  }, [finalTranscript, handleVoiceCommands, setTranscript]);

  return {
    transcript,
    finalTranscript,
    listening,
    browserSupportsSpeechRecognition,
    startListening,
    stopListening,
    isMicrophoneAvailable,
  };
};
