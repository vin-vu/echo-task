import SpeechRecognition, {
  useSpeechRecognition,
} from 'react-speech-recognition';
import { useEffect } from 'react';

export type IntentResponse = {
  intent: 'ADD_TASK' | 'DELETE_TASK' | 'MARK_DONE_TASK';
  taskDescription: string;
}

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

  const fetchIntent = async (transcript: string): Promise<IntentResponse | undefined> => {
    if (transcript.length !== 0) {
      console.log('sending: ', transcript);
      const backendAPI = 'http://localhost:8080/detect-intent';
      try {
        const response = await fetch(backendAPI, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({ transcript }),
        });
        const json = await response.json();
        console.log('response: ', json);
        return json;
      } catch (e) {
        console.error(e);
      }
    }
  };

  useEffect(() => {
    fetchIntent(finalTranscript);
  }, [finalTranscript]);

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
