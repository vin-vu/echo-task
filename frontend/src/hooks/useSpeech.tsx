import SpeechRecognition, {
  useSpeechRecognition,
} from 'react-speech-recognition';
import { useEffect } from 'react';

export const useSpeech = () => {
  const {
    transcript,
    finalTranscript,
    listening,
    browserSupportsSpeechRecognition,
    isMicrophoneAvailable,
  } = useSpeechRecognition();

  const startListening = SpeechRecognition.startListening;
  const stopListening = SpeechRecognition.stopListening;

  const fetchIntent = (transcript: string) => {
    if (transcript.length !== 0) {
      console.log('sending: ', transcript);
    }
    return;
  };

  useEffect(() => {
    fetchIntent(finalTranscript);
  });

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
