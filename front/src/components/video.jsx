import {useEffect} from "react";

export default function Video() {
    useEffect(() => {
        const video = document.getElementById("my-video");
        const c1 = document.getElementById("my-canvas");
        const ctx1 = c1.getContext("2d");
        let width,height

        function timerCallback() {
            if (video.paused || video.ended) {
                return;
            }
            computeFrame();
            setTimeout(() => {
                timerCallback();
            }, 16); // roughly 60 frames per second
        }
        function doLoad() {


            video.addEventListener(
                "play",
                () => {
                    width = video.width;
                    height = video.height;
                    timerCallback();
                },
                false,
            );
        }

        function computeFrame() {
            ctx1.drawImage(video, 0, 0, width, height);
            const frame = ctx1.getImageData(0, 0, width, height);
            const l = frame.data.length / 4;

            for (let i = 0; i < l; i++) {
                const grey =
                    (frame.data[i * 4 + 0] +
                        frame.data[i * 4 + 1] +
                        frame.data[i * 4 + 2]) /
                    3;

                frame.data[i * 4 + 0] = grey;
                frame.data[i * 4 + 1] = grey;
                frame.data[i * 4 + 2] = grey;
            }
            ctx1.putImageData(frame, 0, 0);

            return;
        }
        doLoad()
    }, []);

    return (
        <div>
            <h1> video with mp4 format</h1>
            <video id="my-video" controls width="480" height="270" crossOrigin="anonymous">
                <source
                    src="http://localhost:8080/video"
                    type="video/mp4"/>
            </video>

            <canvas id="my-canvas" width="480" height="270"></canvas>
        </div>
    )
}