import SpeechRecognition, {
  useSpeechRecognition,
} from 'react-speech-recognition';
import { useEffect } from 'react';
import { fetchIntent, IntentResponse } from '../api/Api';

export const useSpeech = (
  handleVoiceCommands: (intentPayload: IntentResponse) => void
) => {
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
  }, [finalTranscript, handleVoiceCommands]);

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
