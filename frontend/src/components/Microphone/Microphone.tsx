import { FaMicrophone } from 'react-icons/fa';
import './Microphone.css';

export default function Microphone() {
  return (
    <div className="mic-container">
      <div className="mic-circle"></div>
      <FaMicrophone className="mic-icon" size={75} />
    </div>
  );
}
