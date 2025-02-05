import SpeechRecognition, {
  useSpeechRecognition,
} from 'react-speech-recognition';

export const useSpeech = () => {
  const {
    transcript,
    finalTranscript,
    listening,
    browserSupportsSpeechRecognition,
    browserSupportsContinuousListening,
    isMicrophoneAvailable,
  } = useSpeechRecognition();

  const startListening = (): void => {
    if (browserSupportsContinuousListening) {
      SpeechRecognition.startListening({ continuous: true });
    } else {
      SpeechRecognition.startListening();
    }
  };

  const stopListening = (): void => {
    SpeechRecognition.stopListening()
  }

  return {
    transcript,
    finalTranscript,
    listening,
    browserSupportsSpeechRecognition,
    startListening,
    stopListening,
    isMicrophoneAvailable,
    browserSupportsContinuousListening,
  };
};
