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

  const fetchIntent = async (transcript: string) => {
    if (transcript.length !== 0) {
      console.log('sending: ', transcript);
      const backendAPI = 'http://localhost:8080/detect-intent';
      try {
        const response = await fetch(backendAPI);
        const json = await response.json();
        console.log('json: ', json)
      } catch (e) {
        console.error(e);
      }
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
