import { useState, useEffect } from 'react';

/**
 * A component that displays messages (error/success) with auto-dismiss functionality
 * @param {string} message - The message to display
 * @param {string} type - The type of message ('error' or 'success')
 * @param {function} onDismiss - Callback to dismiss the message
 */

const MessageDisplay = ({ message, type = 'error', onDismiss }) => {

    useEffect(() => {
        //创建一个 4 秒的定时器（timer）
        const timer = setTimeout(() => {
            onDismiss();
        }, 4000);

        //clearup: clear the timer if component unmounts or message changes
        return () => clearTimeout(timer);

    }, [message, onDismiss]);//当 message 或 onDismiss 改变时，会执行 useEffect


    //Don't render if there's no message
    if (!message) return null;

    const isError = type === 'error';
    const displayClass = isError ? 'error-display': 'success-display';
    const messageClass = isError ? 'error-message': 'success-message';
    const progressClass = isError ? 'error-progress': 'success-progress';

    return (
        <div className={`message-display ${displayClass}`}>
            <div>
                {/* Display the message */}
                <span className={`message-text ${messageClass}`}>{message}</span>
                {/* Visual progress indicator (for auto-dismiss)*/}
                <div className={`message-progress ${progressClass}`}></div>
            </div>
        </div>
    );

   }

    export const useMessage = () => {
        //State to hold the current messages 
        const [errorMessage, setErrorMessage] = useState(null);
        const [successMessage, setSuccessMessage] = useState(null);

        //Shows an error message 
        const showError = (message) => {
            setErrorMessage(message);
        };

        //Shows a success message
        const showSuccess = (message) => {
            setSuccessMessage(message);
        };

        /** Dismisses the current error message */
        const dismissError = () => {
            setErrorMessage(null);
        };

        /** Dismisses the current success message */
        const dismissSuccess = () => {
            setSuccessMessage(null);
        };


        return {
            //component that renders error display 
            ErrorDisplay: () => (
                <MessageDisplay
                    message={errorMessage}
                    type="error"
                    onDismiss={dismissError}
                />
            ),
            //component that renders success display
            SuccessDisplay: () => (
                <MessageDisplay
                    message={successMessage}
                    type="success"
                    onDismiss={dismissSuccess}
                />
            ),
            //Methods to control the display
            showError,
            showSuccess,
            dismissError,
            dismissSuccess


        };

};

export default MessageDisplay;

