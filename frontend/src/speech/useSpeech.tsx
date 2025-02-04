import SpeechRecognition, {
  useSpeechRecognition,
} from 'react-speech-recognition';

export default function useSpeech() {
  const {
    transcript,
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

  return {
    transcript,
    listening,
    browserSupportsSpeechRecognition,
    startListening,
    isMicrophoneAvailable,
  };
}
