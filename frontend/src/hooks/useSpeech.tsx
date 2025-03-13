import SpeechRecognition, {
  useSpeechRecognition,
} from 'react-speech-recognition';
import { useEffect } from 'react';

export type IntentResponse = {
  id: string;
  intent: 'ADD_TASK' | 'DELETE_TASK' | 'COMPLETED_TASK';
  description: string;
  completed: boolean;
};

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
    const fetchIntent = async (
      transcript: string
    ): Promise<IntentResponse | undefined> => {
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
          const json: IntentResponse = await response.json();
          console.log('voice command response: ', json);
          return json;
        } catch (e) {
          console.error(e);
        }
      }
    };

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
